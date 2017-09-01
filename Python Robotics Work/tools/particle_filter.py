import numpy as np
from utility import *


class ParticleFilter(object):

    def __init__(self, **kwargs):
        self.num_particles = kwargs.get('num_particles', 400)
        self.frame_dim = kwargs.get('frame_dim', (640, 360))
        self.l_wall_x = 75
        self.r_wall_x = 564
        self.t_wall_y = 36
        self.b_wall_y = 328
        self.particles = self.init_particles(self.num_particles, self.frame_dim)
        self.weights = self.init_weights(self.num_particles)
        self.sigma_error = kwargs.get('sigma_error', 2.2)
        self.motion_model = kwargs.get('motion_model', 'random')
        self.last_heading = 0
        self.last_location = (0, 0)
        self.last_particle_motion = (0, 0)
        self.last_particle_accel = (0, 0)
        self.accel_change = 0

    def init_particles(self, num_particles, frame_dim):
        """
        Initializes the number of particles within the bounds of the frame
        :param num_particles: total number of particles as int
        :param frame_dim: max x, y dimensions as a tuple of ints
        :return: particles, the initialized array of particles in random uniform distribution
        """
        particles = np.zeros((num_particles, 2), dtype=int)

        particles_x = np.random.randint(0, high=frame_dim[0], size=num_particles)
        particles_y = np.random.randint(0, high=frame_dim[1], size=num_particles)

        particles[:, 0] = particles_x
        particles[:, 1] = particles_y

        return particles

    def init_weights(self, num_particles):
        """
        Initializes weights array with uniform weighting summing to one
        :param num_particles: int
        :return: weights as np.array
        """
        weights = np.ones(num_particles) / num_particles
        return weights

    def motion_update(self, sigma=9):
        if self.motion_model == 'random':
            self.random_motion(sigma)
        elif self.motion_model == 'dynamic':
            if self.is_past_wall():
                self.past_wall_motion()
            elif self.accel_change < -1 and self.is_near_wall():
                self.wall_collision_motion()
            elif self.is_inside_candle():
                self.inside_candle_motion()
            elif self.accel_change < -1 and self.is_near_candle():
                self.candle_collision_motion()
            else:
                # Apply open space motion model
                self.open_space_motion()

    def is_near_wall(self):
        near_wall = self.is_near_l_wall() or self.is_near_r_wall() or self.is_near_t_wall() or \
                    self.is_near_b_wall()
        return near_wall

    def is_near_r_wall(self):
        padding = 8
        near_r_wall = self.last_location[0] > 564 - padding
        return near_r_wall

    def is_near_b_wall(self):
        padding = 8
        near_b_wall = self.last_location[1] > 328 - padding
        return near_b_wall

    def is_near_l_wall(self):
        padding = 8
        near_l_wall = self.last_location[0] < 75 + padding
        return near_l_wall

    def is_near_t_wall(self):
        padding = 8
        near_t_wall = self.last_location[1] < 36 + padding
        return near_t_wall

    def is_past_wall(self):
        past_t_wall = self.last_location[1] < 36
        past_r_wall = self.last_location[0] > 564
        past_b_wall = self.last_location[1] > 328
        past_l_wall = self.last_location[0] < 75
        past_wall = past_t_wall or past_r_wall or past_b_wall or past_l_wall
        return past_wall

    def is_inside_candle(self):
        past_candle = False
        candle_center = (334, 182)
        candle_radius = 31
        if distance_between(candle_center, self.last_location) < candle_radius:
            past_candle = True
        return past_candle

    def is_near_candle(self):
        candle_center = (334, 182)
        candle_radius = 31
        padding = 10
        padded_radius = candle_radius + padding
        if distance_between(candle_center, self.last_location) < padded_radius:
            near_candle = True
        else:
            near_candle = False
        return near_candle

    def compute_last_particle_motion(self, x, y):
        dx = x - self.last_location[0]
        dy = y - self.last_location[1]
        return dx, dy

    def compute_acceleration(self, dx, dy):
        dx2 = dx - self.last_particle_motion[0]
        dy2 = dy - self.last_particle_motion[1]
        return dx2, dy2

    def compute_accel_change(self, dx2, dy2):
        current_accel = np.linalg.norm((dx2, dy2))
        prev_accel = np.linalg.norm(self.last_particle_accel)
        accel_change = current_accel - prev_accel
        return accel_change

    def get_heading(self, current_pt):
        """
        Compute the heading from 0 to 2pi in radians
        :param current_pt: Current (x, y) point to compute heading from
        :return: heading as float in radians in range of 0 to 2pi
        """
        x_tan = current_pt[0] - self.last_location[0]
        y_tan = current_pt[1] - self.last_location[1]
        heading = np.arctan2(y_tan, x_tan)
        heading += (2 * np.pi)
        heading %= (2 * np.pi)

        return heading

    def random_motion(self, sigma):
        """
        Applies random Gaussian motion to all particles
        :param sigma:
        :return:
        """
        motion = np.random.randn(self.particles.shape[0], self.particles.shape[1])
        motion = motion * sigma
        motion = np.array(motion, dtype=int)
        self.particles += motion
        self.mod_out_of_range_particles()

    def uniform_motion(self, dx, dy):
        """
        Applies uniform dx, dy motion update to all particles
        :param dx:
        :param dy:
        :param particles:
        :return:
        """
        self.particles[:, 0] += dx
        self.particles[:, 1] += dy
        self.mod_out_of_range_particles()
        # self.stop_particles_at_wall()

    def compute_dx_dy(self, distance, heading):
        dx = distance * np.cos(heading)
        dx = int(round(dx))
        dy = distance * np.sin(heading)
        dy = int(round(dy))
        return dx, dy

    def open_space_motion(self):
        self.random_motion(3)
        # print('open_space_motion')
        distance = np.random.randn(1) * .8 + 6.5
        if self.is_near_wall() or self.is_near_candle():
            distance /= 2
        distance = np.abs(distance)
        heading_change = np.random.randn(1) / 12 - 0.02
        # print('heading_change = ' + str(heading_change))
        new_heading = self.last_heading + heading_change
        self.last_heading = angle_trunc(new_heading)
        dx, dy = self.compute_dx_dy(distance, new_heading)
        # print('dx = ' + str(dx) + ', dy = ' + str(dy))
        self.uniform_motion(dx, dy)

    def wall_collision_motion(self):
        self.random_motion(3)
        new_heading = self.last_heading
        if self.is_near_t_wall():
            new_heading = -self.last_heading
        if self.is_near_b_wall():
            a2 = self.last_heading - np.pi
            new_heading = np.pi - a2
        if self.is_near_l_wall():
            new_heading = np.pi - self.last_heading
        if self.is_near_r_wall():
            new_heading = np.pi - self.last_heading

        self.last_heading = new_heading
        distance = np.random.randn(1) * 2 + 8
        distance = np.abs(distance)

        dx, dy = self.compute_dx_dy(distance, self.last_heading)
        self.uniform_motion(dx, dy)

    def candle_collision_motion(self):
        self.random_motion(3)
        candle_center = (334, 182)
        candle_radius = 31
        center_heading = get_heading(self.last_location, candle_center)
        opposite_heading = center_heading + 2 * np.pi
        opposite_heading %= (np.pi * 2)

        # Compute the surface angle tangent to the current location
        tangent_angle = center_heading + np.pi / 2
        tangent_angle %= (np.pi * 2)

        # Compute new_heading
        new_heading = self.last_heading
        if 0 < opposite_heading < np.pi/2:
            new_heading = self.last_heading - (tangent_angle - np.pi)
        if np.pi/2 < opposite_heading < np.pi:
            new_heading = np.pi - self.last_heading
            new_heading = new_heading - (np.pi/2 - tangent_angle)
            incidence = tangent_angle - self.last_heading
            new_heading = self.last_heading + 2 * incidence
        if np.pi < opposite_heading < (np.pi + np.pi/2):
            incidence = tangent_angle - self.last_heading
            new_heading = self.last_heading + 2 * incidence
        if (np.pi + np.pi/2) < opposite_heading < 2 * np.pi:
            incidence = tangent_angle - np.pi
            new_heading = self.last_heading - 2 * incidence

        self.last_heading = new_heading
        distance = np.random.randn(1) + 8
        distance = candle_radius - distance_between(self.last_location, candle_center) + np.abs(np.random.randn(1)) + 10
        distance = np.abs(distance)

        dx, dy = self.compute_dx_dy(distance, self.last_heading)
        self.uniform_motion(dx, dy)

    def past_wall_motion(self):
        center = (self.frame_dim[0]/2, self.frame_dim[1]/2)
        new_heading = get_heading(self.last_location, center)
        distance = 14
        dx, dy = self.compute_dx_dy(distance, new_heading)
        self.uniform_motion(dx, dy)

    def inside_candle_motion(self):
        candle_center = (334, 182)
        candle_radius = 31
        center_heading = get_heading(self.last_location, candle_center)
        opposite_heading = center_heading + 2 * np.pi
        opposite_heading %= (np.pi * 2)
        new_heading = opposite_heading + np.random.randn(1)
        distance = candle_radius - distance_between(self.last_location, candle_center) + 10
        dx, dy = self.compute_dx_dy(distance, new_heading)
        self.uniform_motion(dx, dy)

    def mod_out_of_range_particles(self):
        """
        Rolls over particles that are out of range back into the frame by rolling them to the other side of frame
        """
        self.particles[:, 0] %= self.frame_dim[0]
        self.particles[:, 1] %= self.frame_dim[1]

    def stop_particles_at_wall(self):
        padding = 10
        min_x = np.ones(self.particles.shape[0]) * self.l_wall_x - padding
        max_x = np.ones(self.particles.shape[0]) * self.r_wall_x + padding
        min_y = np.ones(self.particles.shape[0]) * self.t_wall_y - padding
        max_y = np.ones(self.particles.shape[0]) * self.b_wall_y + padding

        self.particles[:, 0] = np.where(self.particles[:, 0] < self.l_wall_x, self.particles[:, 0], min_x)
        self.particles[:, 0] = np.where(self.particles[:, 0] > self.r_wall_x, self.particles[:, 0], max_x)
        self.particles[:, 1] = np.where(self.particles[:, 1] < self.t_wall_y, self.particles[:, 1], min_y)
        self.particles[:, 1] = np.where(self.particles[:, 1] > self.b_wall_y, self.particles[:, 1], max_y)

    def sense_update(self, measurement):
        self.update_weights(measurement)
        self.resample_particles()
        x, y = self.average_particle()
        dx, dy = self.compute_last_particle_motion(x, y)
        dx2, dy2 = self.compute_acceleration(dx, dy)

        self.last_heading = self.get_heading((x, y))
        # print('last_heading = ' + str(self.last_heading))
        self.accel_change = self.compute_accel_change(dx2, dy2)
        self.last_location = (x, y)
        self.last_particle_motion = (dx, dy)
        self.last_particle_accel = (dx2, dy2)

    def update_weights(self, measurement):
        """
        Updates the probability weights of the particles based on distance to last measurement
        :param measurement: measured coordinates from sensing location as (x, y) tuple of ints
        :return:
        """
        num_particles = self.particles.shape[0]
        for i in range(num_particles):
            distance = distance_between(self.particles[i], measurement)
            self.weights[i] = np.exp(-distance / self.sigma_error**2)

        # Normalize weights
        normalizer = self.weights.sum()
        self.weights /= normalizer

    def resample_particles(self):
        num_particles = self.particles.shape[0]
        c = np.random.choice(num_particles, size=num_particles, p=self.weights)
        new_particles = self.particles[c]
        self.particles = new_particles

    def average_particle(self):
        """
        Computes the average particle location of all particles
        :return: tuple of floats
        """
        avg_x = np.average(self.particles[:, 0])
        avg_y = np.average(self.particles[:, 1])
        return avg_x, avg_y


def run_pf(data, pf):
    avg_particles = np.zeros(data.shape)
    errors = np.zeros(data.shape[0])
    motions = np.zeros(data.shape[0])
    num_collisions = 0
    for i in range(data.shape[0]):
        pf.motion_update()
        pf.sense_update(data[i])
        avg_particles[i] = pf.average_particle()
        errors[i] = distance_between(data[i], avg_particles[i])
        motions[i] = np.linalg.norm(pf.last_particle_motion)
        # print('pf.last_particle_motion = ' + str(pf.last_particle_motion))
        # print('pf.last_particle_accel = ' + str(pf.last_particle_accel))
        print('Data point ' + str(i) + ' error: ' + str(errors[i]))
        print('acceleration = ' + str(np.linalg.norm(pf.last_particle_accel)))
        print('pf.last_heading = ' + str(pf.last_heading))
        if pf.accel_change < -5 and (pf.is_near_wall() > 0 or pf.is_near_candle()):
            num_collisions +=1
            print('pf.accel_change = ' + str(pf.accel_change))
            print('data[' + str(i) + '] = ' + str(data[i]) + ', avg_particles = ' + str(avg_particles[i]))

    print('num_collisions = ' + str(num_collisions))
    print('average error = ' + str(np.average(errors)))
    motions = motions[1:]
    print(motions)
    print('average motion = ' + str(np.average(motions)))
    print('min motion = ' + str(np.min(motions)))
    print('max motion = ' + str(np.max(motions)))

    return avg_particles, errors


def test_prediction(data, pf, start_predict=1740, num_predictions=60):
    avg_particles = np.zeros(data.shape)
    errors = np.zeros(data.shape[0])
    for i in range(start_predict):
        pf.motion_update()
        pf.sense_update(data[i])
        avg_particles[i] = pf.average_particle()
        errors[i] = distance_between(data[i], avg_particles[i])

    for i in range(start_predict, start_predict + num_predictions):
        pf.motion_update()
        avg_particles[i] = pf.average_particle()
        pf.sense_update(avg_particles[i])
        errors[i] = distance_between(data[i], avg_particles[i])

    return avg_particles, errors


def main():
    data_file = '../inputs/test02.txt'
    data = read_data(data_file)
    pf = ParticleFilter(num_particles=300, motion_model='random')
    # avg_particles, errors = run_pf(data, pf)
    avg_particles, errors = test_prediction(data, pf)
    for i in range(data.shape[0]):
        print('Actual location ' + str(i) + ' : ' + str(data[i]))
        print('Average particle is ' + str(avg_particles[i]))
        print('Error is ' + str(errors[i]))


if __name__ == '__main__':
    main()

