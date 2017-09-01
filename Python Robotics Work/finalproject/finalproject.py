#!/usr/bin/env python

import sys
import os
import numpy as np
from ast import literal_eval
from particle_filter import ParticleFilter


def pf_prediction(indata, psize):
    pf = ParticleFilter(num_particles=150, motion_model='dynamic')
    for measurement in indata:
        pf.motion_update()
        pf.sense_update(measurement)

    odata = [pf.average_particle()]

    for _ in range(1, psize):
        pf.motion_update()
        average = pf.average_particle()
        odata.append(average)
        pf.sense_update(average)
    return odata


def main():
    root = os.path.dirname(os.path.realpath(__file__))
    filename =  sys.argv[1]
    input_file = open(filename, 'r')
    indata = [literal_eval(l.strip()) for l in input_file.readlines()]

    prediction = pf_prediction(indata, 60)
    prediction = np.round(np.array(prediction)).astype(int)  # round to nearest int

    output_file = open('prediction.txt', 'w+')
    for x, y in prediction:
        output_file.write('%d, %d\n' % (x, y))

    input_file.close()
    output_file.close()


if __name__ == '__main__':
    main()
