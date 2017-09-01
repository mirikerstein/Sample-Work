import csv
import os
import numpy as np
import matplotlib.pyplot as plt
from utility import *


def compute_distances(data):
    distances = np.zeros(data.shape[0] - 2)
    for i in range(data.shape[0] - 2):
        distances[i] = distance_between(data[i + 1], data[i + 2])
    return distances


def compute_turn_angles(data):
    turn_angles = np.zeros(data.shape[0] - 2)
    for i in range(data.shape[0] - 2):
        turn_angles[i] = get_turn_angle(data[i], data[i + 1], data[i + 2])
    return turn_angles


def compute_accelerations(distances):
    accelerations = np.zeros(distances.shape)
    for i in range(distances.shape[0] - 1):
        accelerations[i + 1] = distances[i] - distances[i + 1]
    return accelerations


def compute_acceleration_changes(accelerations):
    accel_changes = np.zeros(accelerations.shape[0])
    for i in range(accelerations.shape[0] - 1):
        accel_changes[i + 1] = accelerations[i] - accelerations[i + 1]
    return accel_changes


def main():
    data_file = '../inputs/training_data.txt'
    data = read_data(data_file)

    distances = compute_distances(data)
    turn_angles = compute_turn_angles(data)
    accelerations = compute_accelerations(distances)
    accel_changes = compute_acceleration_changes(accelerations)

    avg_distance = np.average(distances)
    avg_turn_angle = np.average(turn_angles)
    avg_acceleration = np.average(accelerations)
    avg_accel_change = np.average(accel_changes)
    print('avg_distance = ' + str(avg_distance))
    print('avg_turn_angle = ' + str(avg_turn_angle))
    print('avg_acceleration = ' + str(avg_acceleration))
    print('avg_accel_change = ' + str(avg_accel_change))

    # Analyze data greater than distance threshold value
    d_threshold = 7
    distances_gt = []
    turn_angles_gt = []
    for i in range(distances.shape[0]):
        if distances[i] > d_threshold:
            distances_gt.append(distances[i])
            turn_angles_gt.append(turn_angles[i])

    distances_gt = np.array(distances_gt)
    turn_angles_gt = np.array(turn_angles_gt)

    print('Average distance for distances greater than ' + str(d_threshold) + ' is ' + str(np.average(distances_gt)))
    print('Average turn angle for distances greater than ' +
          str(d_threshold) + ' is ' + str(np.average(turn_angles_gt)))
    # plt.hist(turn_angles, bins=21, log=True)
    # plt.show()

    accel_threshold = -4
    distances_above_accel_thresh = []
    distances_below_accel_thresh = []
    turn_angles_above_accel_thresh = []
    for i in range(2, accel_changes.shape[0]):
        if accel_changes[i] > accel_threshold:
            distances_above_accel_thresh.append(distances[i])
            turn_angles_above_accel_thresh.append(turn_angles[i])
        else:
            distances_below_accel_thresh.append(distances[i])

    print('Average distance above acceleration threshold is ' + str(np.average(distances_above_accel_thresh)))
    print('Standard deviation above acceleration threshold is ' + str(np.std(distances_above_accel_thresh)))
    print('Average distance below acceleration threshold is ' + str(np.average(distances_below_accel_thresh)))
    print('Standard deviation below acceleration threshold is ' + str(np.std(distances_below_accel_thresh)))
    print('Average turn angle above acceleration threshold is ' + str(np.average(turn_angles_above_accel_thresh)))
    print('Standard deviation above acceleration threshold is ' + str(np.std(turn_angles_above_accel_thresh)))

    plt.hist(turn_angles_above_accel_thresh, bins=25)
    plt.show()

    print('Min x, y is ' + str(data.min(0)))
    print('Max x, y is ' + str(data.max(0)))

    # with open('training_data_distances.csv', 'w') as csvfile:
    #     csvwriter = csv.writer(csvfile, delimiter=' ', quoting=csv.QUOTE_MINIMAL)
    #     for i in range(distances.shape[0]):
    #         csvwriter.writerow([str(distances[i])])
    #
    # with open('training_data_turn_angles.csv', 'w') as csvfile:
    #     csvwriter = csv.writer(csvfile, delimiter=' ', quoting=csv.QUOTE_MINIMAL)
    #     for i in range(turn_angles.shape[0]):
    #         csvwriter.writerow([str(turn_angles[i])])


if __name__ == '__main__':
    main()

