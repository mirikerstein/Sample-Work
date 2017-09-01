"""
TODO
    write up collision model
    write up candle and wall model
    Resmooth segments individually
    Add acceleration
    Tune noise parameters: velocity_noise, turning_noise
"""

import random
from math import pi, sin, cos, atan2, sqrt, exp
import numpy as np
from analysis import moving_average, unit_mag, velocity, heading, heading_change, acceleration, find_collisions
from analysis import BOX_LT, BOX_LB, BOX_RB, BOX_RT, CANDLE_CENTER, CANDLE_RADIUS, BOUNDS
from intersect import intersect_ray_circle, intersect_ray_segment, cross

DEBUG = False
DTOR = pi / 180
RTOD = 180 / pi

sqrt_2pi = sqrt(2 * pi)


def angle_trunc(a):
    """This maps all angles to a domain of [-pi, pi]"""
    while a < 0.0:
        a += pi * 2
    return ((a + pi) % (pi * 2)) - pi


def intersect_rays_segment(pts, v, b2, e2):
    d2 = e2 - b2
    result = []
    for pt in pts:
        if cross(v, d2) < 0:  # ignore if coming back into box
            continue
        dt = intersect_ray_segment(pt, v, b2, e2)
        if dt is not None:
            result.append(dt)
    return result


def gaussian(x, sigma):
    """
    calculates probability of x for 1-dim Gaussian with mean 0.0 and variance sigma
    """
    return exp(-0.5 * (x / sigma) ** 2) / (sqrt_2pi * sigma)
    # gaussian(x, sigma, mu=0):
    # return exp(-((x - mu)**2) / sigma**2 / 2.0) / sqrt(2 * pi * sigma**2)


def distance(pa, pb):
    return sqrt((pa[0] - pb[0]) ** 2 + (pa[1] - pb[1]) ** 2)


class Particle(object):
    """
    """
    position_noise = 0.0
    velocity_noise = 0.025
    heading_noise = 0.0 * DTOR
    turning_noise = 0.5 * DTOR

    candle_center = np.array(CANDLE_CENTER)
    candle_radius = CANDLE_RADIUS
    box_lt = np.array(BOX_LT)
    box_lb = np.array(BOX_LB)
    box_rt = np.array(BOX_RT)
    box_rb = np.array(BOX_RB)

    def __init__(self, x, y, heading, turning, velocity, weight=0):
        self.x = x
        self.y = y
        self.heading = heading
        self.turning = turning
        self.velocity = velocity
        self.weight = weight
        self.debug = False

    def __str__(self):
        return '%f\t%f\t%f\t%f\t%f' % (
            self.x, self.y, self.heading * RTOD, self.turning * RTOD, self.velocity)

    def clone(self):
        return Particle(self.x, self.y, self.heading, self.turning, self.velocity, self.weight)

    def update(self):
        """This function turns the robot and then moves it forward."""

        x0, y0 = self.x, self.y
        heading = angle_trunc(self.heading + self.turning)
        v_mag = self.velocity
        if self.debug: print "\t%s" % (self)
        vx = v_mag * cos(heading)
        vy = v_mag * sin(heading)
        pt0 = np.array([x0, y0])
        v = np.array([vx, vy])
        #bounds = BOUNDS.bounding_box(pt0, heading)
        bounds = BOUNDS.bounding_box(pt0, heading + self.turning)
        new_v = np.array([vx, vy])

        retard = 0.99  # XXX physics term?

        # See if any corner of the bounding box hits the candle
        dts = []
        for pt in bounds:
            dt = intersect_ray_circle(pt, v, self.candle_center, self.candle_radius)
            if dt is not None:
                dts.append(dt)
        if dts:
            if self.debug: print "HIT CANDLE"
            collision_dt = min(dts)
            ptc = pt0 + v * collision_dt

            a = -v # opposite of velocity
            b = ptc - self.candle_center # center to collision
            # reflect a around b
            # a dot b = b dot c
            # ax * bx + ay * by = cx * bx + cy * by =
            # ax * bx + ay * by - cx * bx - cy * by = 0
            # ax * bx/bx + ay * by/bx = cx * bx/bx + cy * by/bx
            # ax + ay * by/bx = cx + cy * by/bx
            # cx = ax + (ay - cy) * by/bx
            # cy = 1
            # cx = ax + (ay - 1) * by/bx
            cx = a[0] + (a[1] - 1.0) * b[1]/b[0]
            # reflect velocity across center to collision
            new_v = np.array([cx, 1.0]) * (v_mag * retard) / sqrt(1 + cx**2)
            dt = 1.0 - collision_dt
            pt1 = ptc + new_v * dt

            self.x = pt1[0]
            self.y = pt1[1]
            self.velocity = v_mag * retard
            self.heading = atan2(new_v[1], new_v[0])
            self.turning = -self.turning  # XXX ???
            return

        # See if any corner of the bounding box hits a wall
        collision_dt = 1.0
        dts = intersect_rays_segment(bounds, v, self.box_lb, self.box_lt)
        if dts:
            if self.debug: print 'Left Wall'
            collision_dt = min(dts)
            new_v = np.array([-vx, vy])

        dts = intersect_rays_segment(bounds, v, self.box_rt, self.box_rb)
        if dts:
            if self.debug: print 'Right Wall'
            collision_dt = min(dts)
            new_v = np.array([-vx, vy])

        dts = intersect_rays_segment(bounds, v, self.box_lt, self.box_rt)
        if dts:
            dt = min(dts)
            if dt < collision_dt:
                if self.debug: print 'Top Wall'
                collision_dt = dt
                new_v = np.array([vx, -vy])

        dts = intersect_rays_segment(bounds, v, self.box_rb, self.box_lb)
        if dts:
            dt = min(dts)
            if dt < collision_dt:
                if self.debug: print 'Bottom Wall'
                collision_dt = dt
                new_v = np.array([vx, -vy])

        if collision_dt < 1.0:
            #debug('collision')
            dt = 1.0 - collision_dt
            pt1 = pt0 + v * collision_dt + new_v * dt

            self.x = pt1[0]
            self.y = pt1[1]
            self.velocity = v_mag * retard
            self.heading = atan2(new_v[1], new_v[0])
            self.turning = -self.turning  # XXX ???
        else:
            self.x = x0 + vx
            self.y = y0 + vy
            self.heading = heading
            self.velocity = v_mag

    def perturb(self, min_velocity=0.01):
        self.turning = random.gauss(self.turning, self.turning_noise)
        self.velocity = max(min_velocity, random.gauss(self.velocity, self.velocity_noise))
        self.x = random.gauss(self.x, self.position_noise)
        self.y = random.gauss(self.y, self.position_noise)
        self.heading = random.gauss(self.heading, self.heading_noise)

    def blend(self, other, alpha):
        beta = 1.0 - alpha
        self.x = alpha * self.x + beta * other.x
        self.y = alpha * self.y + beta * other.y
        self.heading = alpha * self.heading + beta * other.heading
        self.turning = alpha * self.turning + beta * other.turning
        self.velocity = alpha * self.velocity + beta * other.velocity

    def position(self):
        return (self.x, self.y)
        # return np.array(self.x, self.y)


class ParticleFilter2(object):
    def __init__(self, observations, num_particles=50,
                 smooth_window_size=7, smooth_sigma=1.0,
                 num_to_compare_to=5, position_error=1.0, alpha=0.5,
    ):
        # Estimate states
        data = observations
        data = moving_average(observations, smooth_window_size, smooth_sigma)

        v = velocity(data)
        v_unit, v_mag = unit_mag(v, epsilon=1.0E-6)
        theta = heading(v)
        d_theta = heading_change(theta)
        # a = acceleration(v_mag)

        self.observations = observations
        self.smoothed = data
        self.velocity = v_mag
        self.heading = theta
        self.turning = d_theta

        # Spawn initial particles
        self.num_particles = num_particles #if not DEBUG else 1
        self.particles = self.init_particles()
        self.alpha = alpha
        self.num_to_compare_to = num_to_compare_to
        self.position_error = position_error

    def init_particles(self):
        """
        Initializes the number of particles within the bounds of the frame
        return: particles, the initialized array of particles in random uniform distribution
        """
        index = 0
        x = self.smoothed[index][0]
        y = self.smoothed[index][1]
        heading = self.heading[index]
        turning = self.turning[index]
        velocity = self.velocity[index]

        return [Particle(x, y, heading, turning, velocity)
                for _ in range(self.num_particles)]

    def motion_update(self):
        self.particles[0].debug = DEBUG
        for particle in self.particles:
            particle.update()

    def sense_update(self, measurement):
        for particle in self.particles:
            particle.blend(measurement, self.alpha)

    def importance_weight(self, particle, n, observation, position_error):
        x = particle.x
        y = particle.y
        heading = particle.heading
        turning = particle.turning
        velocity = particle.velocity

        # weight = 1.0
        weight = 0.0
        for i in range(n):
            measurement = self.smoothed[observation - i]
            # weight *= gaussian(distance(measurement, (x, y)), position_error)  # update Gaussian
            weight += gaussian(distance(measurement, (x, y)), position_error) ** 2  # update Gaussian

            # step_reverse  # TODO: do in particle class
            x -= velocity * cos(heading)
            y -= velocity * sin(heading)
            heading -= turning

        weight = sqrt(weight)
        return weight

    def importance_weights(self, n, observation, position_error):
        """ return weights for each particle based on how well they match the measurements """

        if n > observation:
            n = observation + 1

        weight_sum = 0.0
        for particle in self.particles:
            particle.weight = self.importance_weight(particle, n, observation, position_error)
            weight_sum += particle.weight

        result = []
        for particle in self.particles:
            particle.weight /= weight_sum
            result.append(particle.weight)
        return result

    def resample(self, weights):
        """ Resample particles """
        result = []
        index = int(random.random() * self.num_particles)
        max_weight = 2.0 * max(weights)
        beta = 0.0
        for _ in range(self.num_particles):
            beta += random.random() * max_weight
            while beta > weights[index]:
                beta -= weights[index]
                index = (index + 1) % self.num_particles
            # result.append(clone_particle(particles[index]))
            result.append(self.particles[index].clone())
        return result

    def update(self, observation=None):

        if observation is None:
            global DEBUG
            #DEBUG=True
            self.motion_update()
            return

        for particle in self.particles:
            particle.perturb()
        self.motion_update()
        measurement = Particle(
            x=self.smoothed[observation][0],
            y=self.smoothed[observation][1],
            heading=self.heading[observation],
            turning=self.turning[observation],
            velocity=self.velocity[observation],
        )

        self.sense_update(measurement)

        # Add estimate in as last particle
        self.particles[-1] = measurement.clone()

        # Find weights
        weights = self.importance_weights(self.num_to_compare_to, observation, self.position_error)

        # Resample - save particles for next time
        self.particles = self.resample(weights)


    def estimated_position(self):
        """
        Computes the average particle location of all particles
        :return: tuple of floats
        """
        # TODO: Return average, best, average of N best...
        best_particle = self.particles[0]
        for particle in self.particles[1:]:
            if particle.weight > best_particle.weight:
                best_particle = particle
        return best_particle.x, best_particle.y


        '''
        particles = np.array([particle.position() for particle in self.particles])
        avg_x = np.average(particles[:, 0])
        avg_y = np.average(particles[:, 1])
        return avg_x, avg_y
        '''

'''
    #1000
        seed=1 PF2 (866.869070272)	11.552818
        seed=2 PF2 (1093.64897906)	11.494630
        seed=3 PF2 (639.409555421)	11.387432
        seed=4 PF2 (625.790998528)	11.491216
        seed=5 PF2 (596.550940368)	14.007658
        seed=6 PF2 (976.163169411)	11.707432
        seed=7 PF2 (680.527815362)	11.470800
        seed=8 PF2 (994.077111332)	11.317654
        seed=9 PF2 (708.629461508)	11.540844
        seed=10 PF2 (724.200938967)	11.372348
    #1800
        seed=1 PF2 (1448.89718404)	21.230616
        seed=2 PF2 (510.557898605)	20.493867
        seed=3 PF2 (419.817478877)	20.505924
        seed=4 PF2 (654.115845614)	20.442214
        seed=5 PF2 (958.896447361)	20.523993
        seed=6 PF2 (491.087823699)	20.506454
        seed=7 PF2 (1039.86707629)	20.599691
        seed=8 PF2 (1240.54418894)	20.501861
        seed=9 PF2 (1306.19492432)	20.554115
        seed=10 PF2 (1642.36015391)	20.503020
'''
