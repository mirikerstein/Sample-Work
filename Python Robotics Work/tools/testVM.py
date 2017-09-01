#!/usr/bin/python

"""
  [ -i <input_file="training_data.txt">
    -m <method1=s[,method2...]>
    -n <test_cases=1>
    -s <sample_size=1000>
    -o <predictions=60>
    -p
    -h
  ]
"""

import os
import sys
import datetime
import random
from optparse import OptionParser
from math import sqrt, radians, sin, cos
from numpy.linalg import norm
import matplotlib.pyplot as plt
from utility import read_data
from particle_filter import ParticleFilter
import numpy as np

root = os.path.dirname(os.path.realpath(__file__))
idir = "../inputs/"


def main():

    parser = OptionParser()
    parser.add_option("-i", help="input training file", metavar="FILE", default="training_data.txt")
    parser.add_option("-m", help="prediction methods [s n p p2]", default="s")
    #parser.add_option("-s", type="int", help="sample size", default="1800")
    parser.add_option("-s", type="int", help="sample size", default="1000")
    parser.add_option("-o", type="int", help="num prediction points", default="60")
    #parser.add_option("-p", help="plot output", action="store_true", default=False)
    parser.add_option("-p", type="int", help="plot time (0 until return)", default=-1)
    parser.add_option("-S", type="int", help="seed", default="1")
    parser.add_option("-n", type="int", help="num test cases", default="1")

    options, args = parser.parse_args()

    params = {
        'infile': idir + options.i,
        'ntries': options.n,
        'ssize': options.s,
        'osize': options.o,
        'filter': options.m,
        'plot': options.p,
        'seed': options.S,
    }

    run_test_case(params)


def run_test_case(params):
    #infile = params['infile']
    filenames = ['test01.txt', 'test02.txt', 'test03.txt', 'test04.txt',
                     'test05.txt', 'test06.txt', 'test07.txt', 'test08.txt',
                     'test09.txt', 'test10.txt', ]
    filenames = [os.path.join('..', 'inputs', f) for f in filenames]
    for infile in filenames:
      inarray = read_data(infile)
      indata = inarray[:1740]
    # call predict with the sample data array
      odata = pf_prediction(indata, 60)
      odata = np.array(odata)
    # test accuracy of the prediction
      mdata = inarray[1740:1800]
      error = sqrt(sum(norm(mdata - odata, axis=-1) ** 2))
      print("error",error)
            #estimates.append([method_name, error, odata])



def distance(pa, pb):
    return sqrt((pa[0] - pb[0]) ** 2 + (pa[1] - pb[1]) ** 2)


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


if __name__ == '__main__':
    main()
