#!/usr/bin/env python

"""
USAGE: analysis.py [input_file_path ...]
"""

import os
import datetime
import numpy as np
from numpy.linalg import norm
import cv2
import matplotlib.pyplot as plt



class Bounding_Box(object):
    def __init__(self, length, width):
        # initial orientation (0 degree) facing right
        x = length / 2.0
        y = width / 2.0
        self.box = np.array([(-x,-y), (x,-y), (x,y), (-x,y)])

    def bounding_box(self, pos, heading):
        """ return bounding box """
        return np.array(pos) + np.dot(self.box, rotation_matrix(heading))

    def bounding_boxes(self, pos_data, headings):
        return np.array([self.bounding_box(pos, heading)
                         for pos, heading in zip(pos_data, headings)]).astype(np.int)

# -----------------------------------------------------------------------------
# Data analysis
# -----------------------------------------------------------------------------
def stats(array):
    """ Return min, mean, max, std of array """
    return np.min(array), np.mean(array), np.max(array), np.std(array)


def histogram(label, data, bins, display=False):
    """ TODO """
    data = data.flatten()
    bins = sorted(bins)
    hist = np.histogram(data, bins)
    below = len(np.where(data < bins[0])[0])
    above = len(np.where(data > bins[-1])[0])

    print('\n')
    print('%s' % label)
    statistics = '\tMin: %f\tMean: %f\tMax: %f\tStd: %f' % stats(data)
    print statistics
    if below:
        print('%4s %6d' % ('<', below))
    for key, value in zip(hist[1], hist[0]):  # TODO replace with stack
        if value:
            print('%4d %6d' % (key, value))
    if above:
        print('%4s %6d' % ('>', above))

    if display:
        plt.hist(data, bins=len(bins), range=(bins[0], bins[-1]), log=False, alpha=0.6)
        plt.title('%s\n(%s)' % (label, statistics.replace('\t', ' ')))
        plt.show()


# -----------------------------------------------------------------------------
# Data processing
# -----------------------------------------------------------------------------
def read_file(filename):
    """
    Read a input file from final project
    Return  a numpy array of x/y points.  Start and end can be set to return a subset of the data
    """
    data = [line.split(',') for line in open(filename)]
    return np.array(data)


def rotation_matrix(theta):
    cos_theta = np.cos(theta)
    sin_theta = np.sin(theta)
    return np.array(
        [[ cos_theta, sin_theta],
         [-sin_theta, cos_theta]]
    )


def compass(angle):
    """
    Return the compass direction for an angle in degrees
    (-180:W -135:SW -90:S -45:SE 0:E 45:NE 90:N 135:NW 180:W)

    >>> compass(10)
    'E'
    >>> compass(136)
    'NW'
    >>> compass(-180)
    'W'
    """
    assert -180 <= angle <= 180
    angle -= 22.5
    if angle < -180: return 'W'
    if angle < -135: return 'SW'
    if angle <  -90: return 'S'
    if angle <  -45: return 'SE'
    if angle <    0: return 'E'
    if angle <   45: return 'NE'
    if angle <   90: return 'N'
    if angle <  135: return 'NW'
    if angle <  180: return 'W'


def moving_average(data, window_size, sigma=1.0):
    """
    Return the moving avarage of data using a window_size kernel

    TODO: http://scipy.github.io/old-wiki/pages/Cookbook/SavitzkyGolay
    data = savitzky_golay(data, window_size=9, order=2)

    :param data: data to smooth
    :param window_size: size of the kernel to use
    :param sigma:  if sigma, use a guassian kernel, else use a box filter
    :return: smoothed data of same length
    """
    w = window_size // 2
    if not sigma:  # Box Filter
        kernel = np.ones(window_size) / float(window_size)
    else:  # Gaussian Filter
        kernel = cv2.getGaussianKernel(window_size, sigma).T[0]  # normalized

    result = data[:, :]
    for i in range(data.shape[1]):
        result[w:-w:, i] = np.convolve(data[:, i], kernel, 'valid')
    return result


def velocity(vertices):
    """
    Return the velocity unit vector and the magnitude calculated from the vertices
    Pad the end with a copy of the last value (constant velocity)
    :param vertices: array of two dimensional points
    :return:
      v_unit: Array of two dimension points with a magnitude = 1
      v_mag: Array of scalar magnitudes
    """
    v = np.diff(vertices, axis=0)
    v = np.insert(v, -1, v[-1:, :], axis=0)  # Pad last value
    return v


def unit_mag(data, epsilon=1.0E-6):
    """
    Return the unit vector and the magnitude calculated from the data
    :param data: array of two dimensional points
    :param epsilon:  Cap anything below this to avoid divide by zero
    :return:
      unit: Array of unit vectors (same shape as data) with a magnitude = 1
      mag: Array of scalar magnitudes
    """
    mag = np.maximum(norm(data, axis=-1), epsilon)
    unit = data / np.expand_dims(mag, -1)
    return unit, mag


def acceleration(v_mag, pad_value=0.0):
    """
    Return the acceleration given velocity magnitude
    :param v_mag: Array of scalar magniudes
    :param pad_value: Pads the first value, so result matches vertices
    :return: array of scalar acceleration
    """
    result = np.diff(v_mag, axis=0)
    result = np.insert(result, 0, [pad_value])
    return result


def heading(v):
    """ TODO """
    return np.arctan2(v[:, 1], v[:, 0])


def heading_change(theta):
    """
    :param theta: heading
    :return: array of heading change same size as theta array
    """
    d_theta = np.diff(theta)
    d_theta = np.insert(d_theta, -1, d_theta[-1])
    err = abs(d_theta) > np.pi
    d_theta[err] += np.where(theta[err] > 0, 2*np.pi, -2*np.pi)
    return d_theta


def cos_d_theta(v_unit, pad_value=1.0):
    """
    Return cosine of angular velocity -calculated from the unit vectors between vertices
      angular velocityh for vertice[1] is the angle between the vector from vertice[0] to vertice[1],
                                                        and the vector from vertice[1] to vertice[2]
    Assumes v_unit last value was padded to keep it the same length as vertices
    :param pad_value: Pads the first value, so result matches vertices
    :param v_unit: array of unit vectors
    :return: array of angular velocity (radians)
    """
    v1 = v_unit[:-1, :]
    v2 = v_unit[1:, :]
    v1_dot_v2 = (v1 * v2).sum(1)
    result = np.minimum(np.maximum(-1, v1_dot_v2), 1.0)
    result = np.insert(result, 0, [pad_value])
    return result


# -----------------------------------------------------------------------------
# Drawing
# -----------------------------------------------------------------------------
white = (255, 255, 255)
red = (0, 0, 255)
green = (0, 255, 0)
blue = (255, 0, 0)
magenta = (255, 0, 255)
yellow = (0, 255, 255)
orange = (0, 128, 255)
cyan = (255, 255, 0)
d_green = (0, 128, 0)
l_green = (162, 255, 162)


def draw_lines(image, points, color, thick=1, line_type=cv2.LINE_AA, arrow_head=0.0):
    """ Draw lines between all the points on image """
    last_pt = tuple(points[0]) if len(points) else None
    if not arrow_head:
        for pt in points:
            pt = tuple(pt)
            cv2.line(image, last_pt, pt, color, thick, line_type)
            last_pt = pt
    else:
        for pt in points:
            pt = tuple(pt)
            cv2.arrowedLine(image, last_pt, pt, color, thick, line_type, tipLength=arrow_head)
            last_pt = pt


def draw_points(image, points, color, size=1):
    """ Draw filled circles for all points on image """
    for pt in points:
        cv2.circle(image, tuple(pt), size, color, -1)


'''
def draw_hull(image, filenames, color, scale):
    from scipy.spatial import ConvexHull
    combined = []
    for filename in filenames:
        data = [line.split(',') for line in open(filename)]
        combined += [(int(x), int(y)) for x, y in data]
    data = np.array(combined)
    data *= scale

    hull = ConvexHull(data)
    vertices = hull.vertices
    vertices = np.insert(vertices, -1, vertices[0])
    hull = np.array(data[vertices, :]).astype(np.int)
    draw_lines(image, hull.astype(np.int), color, 1)
'''

# CANDLE and BOX for test01
# TODO: Find for other tests
IMG_HEIGHT = 360
IMG_WIDTH = 640
BUG_LENGTH = 36
BUG_WIDTH = 15
BOX_LT = ( 75,  36)
BOX_LB = ( 76, 335)
BOX_RB = (572, 328)
BOX_RT = (564,  30)
CANDLE_CENTER = (334, 182)
CANDLE_RADIUS = 31

PAD_CANDLE_RADIUS = 31 + BUG_LENGTH/2
PAD_BOX_LT = (BOX_LT[0]+BUG_LENGTH/2, BOX_LT[1]+BUG_LENGTH/2)
PAD_BOX_LB = (BOX_LB[0]+BUG_LENGTH/2, BOX_LB[1]-BUG_LENGTH/2)
PAD_BOX_RB = (BOX_RB[0]-BUG_LENGTH/2, BOX_RB[1]-BUG_LENGTH/2)
PAD_BOX_RT = (BOX_RT[0]-BUG_LENGTH/2, BOX_RT[1]+BUG_LENGTH/2)

BOUNDS = Bounding_Box(BUG_LENGTH, BUG_WIDTH)



def draw_box(image, color, scale=1.0):
    """ Draw the final project box """
    box = np.array([BOX_LT, BOX_LB, BOX_RB, BOX_RT, BOX_LT]) * scale
    box = box.astype(np.int)
    draw_lines(image, box, color, 1)
    box = np.array([PAD_BOX_LT, PAD_BOX_LB, PAD_BOX_RB, PAD_BOX_RT, PAD_BOX_LT]) * scale
    box = box.astype(np.int)
    draw_lines(image, box, color, 1)


def draw_candle(image, color, scale=1.0):
    """ Draw the final project candle """
    center = int(CANDLE_CENTER[0] * scale), int(CANDLE_CENTER[1] * scale)
    radius = int(CANDLE_RADIUS * scale)
    cv2.circle(image, center, radius, color, 1, cv2.LINE_AA)
    radius = int(PAD_CANDLE_RADIUS * scale)
    cv2.circle(image, center, radius, color, 1, cv2.LINE_AA)


def describe_collision(bitmask):
    result = ''
    if bitmask == 0:
        return 'None'
    if bitmask == 1:
        result = 'Unknown'
    if bitmask & 8:
        result += 'Top '
    if bitmask & 16:
        result += 'Bottom '
    if bitmask & 2:
        result += 'Left'
    if bitmask & 4:
        result += 'Right'
    if bitmask == 32:
        result += 'Candle'
    return result

def write_collisions(test_name, raw_data, data, v, theta, d_theta, bounds, collisions):
    fp = open('collision_%s.csv' % test_name, 'w')
    fp.write(', '.join([
        'Frame',
        'Raw X', 'Raw Y',
        'X', 'Y',
        'Heading',
        'Velocity 0', 'dHeading 0',
        'Velocity 1', 'dHeading 1',
        'L', 'R', 'T', 'B',
        'Collision BitMask', 'Collision'
    ]) + '\n')


    num_frames = len(raw_data)
    for f in range(1, num_frames):  #TODO: Truncated first frame because of velocity requirment
        fp.write(', '.join([str(s) for s in [
            f,
            raw_data[f, 0], raw_data[f, 1],
            data[f, 0], data[f, 1], theta[f],
            v[f-1], d_theta[f-1],
            v[f], d_theta[f],
            np.min(bounds[f][:,0]), np.max(bounds[f][:,0]), np.min(bounds[f][:,1]), np.max(bounds[f][:,1]),
            collisions[f], describe_collision(collisions[f]),
        ]]) + '\n')


def extract_image(test_name, extract_frame=0):
    """ TODO """

    # Loop over video (till last frame or Ctrl+C is presssed)
    video = cv2.VideoCapture('../videos/%s.mp4' % test_name)

    frame_num = 0
    okay, image = video.read()  # Try to read a frame

    while okay and frame_num <= extract_frame:  # more frames and can read video
        frame_num += 1
        okay, image = video.read()
    return image


def video(data, bounds, collision, test_name, do_video_out=True, fps=10.0, tail=8):
    """
    overlay data on top of test video from Final project
    Draw box and candle and turn them red it a collision is detected
    """

    # Loop over video (till last frame or Ctrl+C is presssed)
    video = cv2.VideoCapture('../videos/%s.mp4' % test_name)

    frame_num = 0
    okay, image = video.read()  # Try to read a frame

    video_out = None
    if okay and do_video_out:
        # Define the codec and create VideoWriter object
        filename = '%s.avi' % test_name
        fourcc = cv2.VideoWriter_fourcc(*'XVID')
        dim = image.shape[1], image.shape[0]
        video_out = cv2.VideoWriter(filename, fourcc, fps, dim)

    # video = None
    # image = np.ones_like(image) * 255
    while okay:  # more frames and can read video
        try:
            if frame_num < len(data):
                color = red if collision[frame_num] else blue
                draw_box(image, color)
                draw_candle(image, color)

                points = data[frame_num - tail:frame_num]
                draw_lines(image, points, green, arrow_head=0.2)

                box = bounds[frame_num]
                box = np.insert(box, 4, box[0], axis=0)
                draw_lines(image, box, green)

            if video_out:
                video_out.write(image)
            else:
                # write images to a directory named for test_name
                cv2.imwrite('%s/frame%03d.jpg' % (test_name, frame_num), image)

            # Update frame number
            frame_num += 1
            if video:
                okay, image = video.read()
            else:
                okay = frame_num < len(data)
                image = np.ones_like(image) * 255

        except KeyboardInterrupt:  # press ^C to quit
            break

    if video_out:
        cv2.destroyAllWindows()
        video_out.release()


def segment(data, collisions, slow, accelerating):
    """
    """
    result = []
    index = 0
    length = len(data)
    while index < length:
        start_index = index
        #while index < length and collisions[index] < 1:  # wait for sharp turn
        while index < length and collisions[index] <= 1:  # wait for sharp turn
            index += 1
        index += 1
        end_index = index
        result.append([start_index, end_index])  # add subtrajectory
        #while index < length and slow[index]:  # wait for speeds up
        while index < length and not accelerating[index]:  # wait for speeds up
            index += 1
    return result


def find_collisions(bounds, scale, v, v_unit, a, cos_d_theta_threshold, pad=8):
    collisions = cos_d_theta(v_unit) < cos_d_theta_threshold

    prev_v = v[:-1,:]
    prev_v = np.insert(prev_v, 0, prev_v[0,:], axis=0)

    box = np.empty_like(bounds)
    box[:,0] = bounds[:,0] + prev_v
    box[:,1] = bounds[:,1] + prev_v
    box[:,2] = bounds[:,2] + prev_v
    box[:,3] = bounds[:,3] + prev_v

    candle_pos = np.array(CANDLE_CENTER) * scale
    candle_radius = CANDLE_RADIUS * scale
    box_to_candle = np.empty_like(box)
    box_to_candle[:,0] = candle_pos - box[:,0]
    box_to_candle[:,1] = candle_pos - box[:,1]
    box_to_candle[:,2] = candle_pos - box[:,2]
    box_to_candle[:,3] = candle_pos - box[:,3]
    box_to_candle_range = np.hypot(box_to_candle[:,:,0], box_to_candle[:,:,1])
    hit_candle = np.min(box_to_candle_range[:], axis=1) < candle_radius + pad

    # XXX: Assumes wall is rectangular
    # TODO: given A, B from wall and X from box, check if A-B cross A-X < 0
    l_wall = ((BOX_LT[0] + BOX_LB[0])/2+pad) * scale
    r_wall = ((BOX_RT[0] + BOX_RB[0])/2-pad) * scale
    t_wall = ((BOX_LT[1] + BOX_RT[1])/2+pad) * scale
    b_wall = ((BOX_LB[1] + BOX_RB[1])/2-pad) * scale

    hit_l_wall = np.min(box[:,:,0], axis=1) < l_wall
    hit_r_wall = np.max(box[:,:,0], axis=1) > r_wall
    hit_t_wall = np.min(box[:,:,1], axis=1) < t_wall
    hit_b_wall = np.max(box[:,:,1], axis=1) > b_wall

    return (
        (collisions) * 1 +
        (collisions & hit_l_wall) * 2 +
        (collisions & hit_r_wall) * 4 +
        (collisions & hit_t_wall) * 8 +
        (collisions & hit_b_wall) * 16 +
        (collisions & hit_candle) * 32)


def analyze(
    filename, image=None, scale=3,
    smooth_window_size=3, smooth_sigma=0.0,
    vel_threshold=10.0, acc_threshold=5.0, d_theta_threshold=45.0,
    draw_raw=False,
    do_scatter=False,
    do_overlay=False,
    do_video=False,
    show_histograms=True,
    do_normalize=False,
    write_collision_file=False,
):
    """
    :param filename:
    :param image:
    :param do_scatter:
    :param scale:
    :param smooth_window_size:
    :param smooth_sigma:
    :param vel_threshold:
    :param acc_threshold:
    :param d_theta_threshold:
    :param draw_raw:
    :param do_overlay:
    :param do_video:
    :param show_histograms:
    :param write_collision_file:
    :param do_normalize:
    """
    test_name = os.path.splitext(os.path.split(filename)[-1])[0]
    print test_name

    if do_overlay:
        scale = 1

    # Scale inputs
    img_height = IMG_HEIGHT * scale
    img_width = IMG_WIDTH * scale

    cos_d_theta_threshold = np.cos(np.radians(d_theta_threshold))
    vel_threshold *= scale
    acc_threshold *= scale

    raw_data = read_file(filename).astype(np.int)
    raw_data *= scale
    data = raw_data.copy()

    # Get image
    if image is None:
        image = np.ones(shape=(img_height, img_width, 3), dtype=np.uint8) * 255

    draw_candle(image, blue, scale)
    draw_box(image, blue, scale)

    if do_overlay:
        alpha = 0.5
        background = extract_image(test_name)
        cv2.addWeighted(background, alpha, image, 1.0-alpha, 0, image)

    image0 = image

    # Smooth
    if smooth_window_size:
        if draw_raw:
            # draw a faint version of raw data
            if do_scatter:
                draw_points(image, data, l_green, 2)
            else:
                draw_lines(image, data, l_green, arrow_head=0.1)

        data = moving_average(data, smooth_window_size, smooth_sigma)

    # Estimate states
    v = velocity(data)
    v_unit, v_mag = unit_mag(v, epsilon=1.0E-6)
    theta = heading(v)
    d_theta = heading_change(theta)
    a = acceleration(v_mag)
    #bounds = BOUNDS.bounding_boxes(data, theta)
    bounds = BOUNDS.bounding_boxes(data, theta + 1.5*d_theta)
    collisions = find_collisions(bounds, scale, v, v_unit, a, cos_d_theta_threshold)
    collisions[(collisions > 0) & (a > 0)] = 0
    slow = v_mag < vel_threshold
    accelerating = a > 0
    # accelerating = a > acc_threshold
    # decelerating = a < -acc_threshold

    if show_histograms:
        # Print out statistics and histograms of calculated states for thresholding
        histogram('Theta', np.degrees(theta), np.arange(-180, 180 + 2*15, 15))
        histogram('DTheta', np.degrees(d_theta), np.arange(-180, 180 + 2*15, 15))
        histogram('Velocity', v_mag, np.arange(0, 12 + 2*0.5, 0.5))
        histogram('Acceleration', a, np.arange(-3, 3 + 2*0.5, 0.5))

    if write_collision_file:
        write_collisions(test_name, raw_data, data, v_mag, theta, d_theta, bounds, collisions)

    image  = image0.copy()
    if do_scatter:
        draw_points(image, data, d_green, 2)
    else:
        draw_lines(image, data, d_green, arrow_head=0.2)
    draw_points(image, data[(collisions & (2|4))>0],  red, 5)      # Left / Right
    draw_points(image, data[(collisions & (8|16))>0], magenta, 5)  # Top / Bottom
    draw_points(image, data[(collisions & (32))>0],   orange, 5)   # Candle
    draw_points(image, data[(collisions == 1)],       blue, 5)     # Anomaly
    cv2.imwrite('%s.jpg' % test_name, image)

    if do_video:
        video(raw_data/scale, bounds, collisions, test_name, do_video_out=True, fps=10.0)

    segments = segment(data, collisions, slow, accelerating)

    #TODO: Resmooth subgtrajectories so that points after collision don't mess up points before

    '''
    image  = image0.copy()
    cols = []
    last_end = None
    for start, end in segments:
        if last_end:
            cols.append([last_end-1, start+1])
        last_end = end

    for start, end in segments:
        sub = data[start:end]
        #sub = moving_average(raw_data[start:end], smooth_window_size, smooth_sigma)
        draw_points(image, [sub[0]], green, 6)
        draw_points(image, [sub[-1]], red, 6)
        draw_lines(image, sub, d_green, arrow_head=0.2)
    cv2.imwrite('%s_segments.jpg' % test_name, image)

    image  = image0
    for start, end in cols:
        sub = data[start:end]
        draw_points(image, [sub[0]], green, 6)
        draw_points(image, [sub[-1]], red, 6)
        draw_lines(image, sub, d_green, arrow_head=0.2)
    cv2.imwrite('%s_collisions.jpg' % test_name, image)
    '''

    '''
    d_theta2 = []
    v_mag2 = []
    for start, end in segments:
        d_theta2 += list(d_theta[start:end-1])
        v_mag2 += list(v_mag[start:end-1])
    d_theta2 = np.array(d_theta2)
    v_mag2 = np.array(v_mag2)
    histogram('DTheta', np.degrees(d_theta2), np.arange(-180, 180 + 2*15, 15))
    histogram('Velocity', v_mag2, np.arange(0, 12 + 2*0.5, 0.5))
    '''


    if do_normalize:
        min_length = 20
        img_height = IMG_HEIGHT*3
        img_width = IMG_WIDTH*3
        image = np.ones(shape=(img_height, img_width, 3), dtype=np.uint8) * 255
        fp = open('%s_normalized.tab' % test_name, 'w')
        for start, end in segments:

            if start + min_length > end:
                continue
            end = start + min_length

            sub = data[start:end]
            sub = sub.astype(np.float)
            sub -= data[start]
            sub = np.dot(sub, rotation_matrix(theta[start]))
            sub /= v_mag[start]

            sub_dir = np.diff(sub, axis=0)
            dir = np.degrees(np.arctan2(sub_dir[:,1], sub_dir[:,0]))
            dir = np.insert(dir, -1, dir[-1])  # Pad last value
            for index, (x,y) in enumerate(sub):
                fp.write('%f\t%f\t%s\n' % (x, y, compass(dir[index])))
            fp.write('\n')

            sub *= 20
            sub = sub.astype(np.int)
            sub += np.array([0, img_height/2])

            in_group = True
            in_group = a[start] > 2
            in_group = d_theta[start] > 0
            color = d_green if in_group else blue

            #TODO: Resmooth subgtrajectories so that points after collision don't mess up points before
            # draw_points(image, [sub[0]], green, 6)
            draw_points(image, [sub[-1]], red, 6)
            draw_lines(image, sub, color, arrow_head=0.2)


        cv2.imwrite('%s_normalized.jpg' % test_name, image)

        '''
        start, end = 1000, 2000
        #x = np.arange(start, end)
        #x = np.degrees(theta[start:end])
        x = a[start:end]
        #y = a[start:end]
        y = np.degrees(d_theta[start:end])
        from plot import plot_array
        plot_array([(x, y, 'g*', 'DTheta')])
        '''



def main():
    import sys

    if len(sys.argv) > 1:
        filenames = sys.argv[1:]
    else:
        filenames = ['test01.txt', 'test02.txt', 'test03.txt', 'test04.txt',
                     'test05.txt', 'test06.txt', 'test07.txt', 'test08.txt',
                     'test09.txt', 'test10.txt', ]
        filenames += ['training_data.txt']
        filenames = [os.path.join('..', 'inputs', f) for f in filenames]
        #filenames = filenames[:1]
        filenames = filenames[-1:]


    for filename in filenames:
        image = None

        analyze(
            filename,
            image,
            scale=1,
            smooth_window_size=7,
            smooth_sigma=1.0,

            d_theta_threshold=20,
            vel_threshold=4.0,  # These numbers are scaled
            acc_threshold=2.0,

            draw_raw=False,
            do_scatter=False,
            do_overlay=False,
            do_video=False,
            do_normalize=False,
            show_histograms=True,
            write_collision_file=False,
        )


if __name__ == '__main__':
    main()

