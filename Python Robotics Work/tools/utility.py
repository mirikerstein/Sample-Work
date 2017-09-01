"""
This file contains utility functions which we can use for our final project
"""

import numpy as np
import time
import matplotlib.pyplot as plt


def read_data(data_file):
    """
    This function reads a text file of x, y coordinates into a numpy array ordered from top to bottom
    :param data_file: string of path to text file with new line separated coordinates
    :return: coordinate_array as np.array of floats
    """
    with open(data_file) as f:
        lines = f.read().splitlines()
    f.close()

    coordinate_array = np.zeros((len(lines), 2))

    for i in range(len(lines)):
        coordinate = lines[i].split(',')
        x = int(coordinate[0])
        y = int(coordinate[1])
        coordinate_array[i, 0] = x
        coordinate_array[i, 1] = y

    return coordinate_array


def read_test_results(test_results):
    with open(test_results) as f:
        lines = f.read().splitlines()
    f.close()

    errors = np.zeros(len(lines))

    for i in range(len(lines)):
        error = lines[i].split()[2]
        error = error[1:-1]
        errors[i] = float(error)

    return errors


def read_file(filename, start=0, end=-1):
    """
    Read a input file from final project
    Return  a numpy array of x/y points
     """
    data = [line.split(',') for line in open(filename)]
    return np.array([(int(x), int(y)) for x, y in data[start:end]])


def get_heading(pt_1, pt_2):
    """
    Compute the heading from -pi to pi in radians
    :param pt_1: Previous (x, y) point to compute heading from
    :param pt_2: Current (x, y) point to compute heading from
    :return: heading as float in radians in range of -pi to pi
    """
    x_tan = pt_2[0] - pt_1[0]
    y_tan = pt_2[1] - pt_1[1]
    heading = np.arctan2(y_tan, x_tan)
    return heading


def get_turn_angle(pt_1, pt_2, pt_3):
    """
    Compute the normalized turn angle in radians.
    0 value means the robot is not turning.
    Positive value means the robot is turning left.
    Negative value means the robot is turning right.
    :param pt_1: First (x, y) point to compute the heading from
    :param pt_2: Second (x, y) point to compute the heading from
    :param pt_3: Third (x, y) point to compute the heading from
    :return: turn angle in radians
    """
    h1 = get_heading(pt_1, pt_2)
    h2 = get_heading(pt_2, pt_3)
    angle = h2 - h1
    angle = angle_trunc(angle)
    return angle


def distance_between(point1, point2):
    """Computes distance between point1 and point2. Points are (x, y) pairs."""
    x1, y1 = point1
    x2, y2 = point2
    return np.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)


def time_function(num_runs, f, *args):
    """
    Utility function to time function run time.  Use for optimization.
    :param num_runs:  Number of loops to run for function
    :param f: function to be timed
    :param args: arguments for function f
    """
    start = time.time()
    for i in range(num_runs):
        f(*args)
    end = time.time()
    run_time = end - start
    print('Run time for ' + str(f) + ' is ' + str(run_time) + ' seconds')


def angle_trunc(a):
    """This maps all angles to a domain of [-pi, pi]"""
    while a < 0.0:
        a += np.pi * 2
    a += np.pi
    a %= np.pi * 2
    a -= np.pi
    return a


if __name__ == '__main__':
    test_results_file = '../test_runs/d2_400p_100n.txt'
    errors = read_test_results(test_results_file)
    print('Average error = ' + str(np.average(errors)))

