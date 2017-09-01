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
from prediction import *
from analysis import BOX_LT, BOX_LB, BOX_RB, BOX_RT, CANDLE_CENTER, CANDLE_RADIUS
from analysis import PAD_BOX_LT, PAD_BOX_LB, PAD_BOX_RB, PAD_BOX_RT, PAD_CANDLE_RADIUS

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
    num_tests = params['ntries']
    infile = params['infile']
    ssize = params['ssize']
    psize = params['osize']
    methods = params['filter']
    plot_time = params['plot']
    seed0 = params['seed']

    all_methods = {
        's': ('Stupid', stupid_prediction),
        'n': ('Naive', naive_prediction),
        'p': ('PF', pf_prediction),
        'p2': ('PF2', pf_prediction2),
        #'p3': ('PF2', pf_prediction3),
    }
    methods = methods.split(',')
    # print("Predction Methodoloy : " + method)

    inarray = read_data(infile)
    n = len(inarray)
    if n < ssize + psize:
        print (" Sample size exceeds training data")
        return

    for i in range(num_tests):
        estimates = []
        for method in methods:
            method_name, predict = all_methods[method]
            seed = seed0 + i
            random.seed(seed)
            np.random.seed(seed)
            t0 = datetime.datetime.now()

            index = int(random.random() * (n - (ssize + psize)))
            # slice ssize of data for test n times from random index
            indata = inarray[index:index + ssize]
            # print("indata=", indata)

            # call predict with the sample data array
            odata = predict(indata, psize)
            odata = np.array(odata)

            # test accuracy of the prediction
            mdata = inarray[index + ssize:index + ssize + psize]
            # error = sqrt(sum([distance(odata[k], mdata[k])**2 for k in range(psize)]))
            error = sqrt(sum(norm(mdata - odata, axis=-1) ** 2))

            print 'seed=%d %s (%s)\t%f' % (
                seed, method_name, error, (datetime.datetime.now() - t0).total_seconds())
            estimates.append([method_name, error, odata])
        # print

        if plot_time >= 0:
            plot(indata, mdata, estimates, plot_time)


def distance(pa, pb):
    return sqrt((pa[0] - pb[0]) ** 2 + (pa[1] - pb[1]) ** 2)


def plot(indata, measurement, estimates, plot_time=0):
    symbol = 'r.', 'g-', 'c-', 'y-*', 'm-', 'o-', 'b-'

    fig = plt.figure()
    plt.title(' '.join(sys.argv[1:]))

    candle = np.array([(CANDLE_CENTER[0] + CANDLE_RADIUS * cos(radians(theta)),
                        CANDLE_CENTER[1] + CANDLE_RADIUS * sin(radians(theta)))
                       for theta in range(0, 365, 5)])
    candle2 = np.array([(CANDLE_CENTER[0] + PAD_CANDLE_RADIUS * cos(radians(theta)),
                         CANDLE_CENTER[1] + PAD_CANDLE_RADIUS * sin(radians(theta)))
                        for theta in range(0, 365, 5)])
    box = np.array([BOX_LT, BOX_LB, BOX_RB, BOX_RT, BOX_LT])
    box2 = np.array([PAD_BOX_LT, PAD_BOX_LB, PAD_BOX_RB, PAD_BOX_RT, PAD_BOX_LT])
    plt.plot(candle[:, 0], candle[:, 1], 'b-')
    plt.plot(candle2[:, 0], candle2[:, 1], 'b-')
    plt.plot(box[:, 0], box[:, 1], 'b-')
    plt.plot(box2[:, 0], box2[:, 1], 'b-')

    m = np.array(indata[-4:])
    plt.plot(m[:, 0], m[:, 1], 'r*', label='Init')
    m = np.array(measurement)
    plt.plot(m[:, 0], m[:, 1], symbol[0], label='Measurement')

    for index, (method, error, odata) in enumerate(estimates):
        e = np.array(odata)
        plt.plot(e[:, 0], e[:, 1], symbol[index + 1], label='%s(%s)' % (method, error))

    # Shrink current axis by 20%
    ax = plt.subplot(111)
    box = ax.get_position()
    ax.set_position([box.x0, box.y0, box.width * 0.8, box.height])

    # Put a legend to the right of the current axis
    ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))

    plt.draw()
    if not plot_time:
        plt.pause(1)
        raw_input("<Hit Enter To Close>")
    else:
        plt.pause(plot_time)
    plt.close(fig)


if __name__ == '__main__':
    main()
