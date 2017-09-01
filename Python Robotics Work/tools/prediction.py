#!/usr/bin/python


import numpy as np
from particle_filter import ParticleFilter
from particle_filter2 import ParticleFilter2


def stupid_prediction(indata, psize):
    """ Assume the bot doesn't move """
    return [indata[-1] for _ in range(psize)]


def naive_prediction(indata, psize):
    """ Assume the bot continues with last instantaneious velocity """
    indata = np.array(indata)
    x = indata[-1]
    dx = x - indata[-2]

    odata = []
    for _ in range(psize):
        # x += dx  #XXX Why doesn't this work?
        x = x + dx
        odata.append(x)
    return odata


def pf_prediction0(indata, psize):
    data = np.array(indata)
    pf = ParticleFilter(num_particles=400)
    avg_particles = np.zeros(data.shape)
    for i in range(len(data)):
        pf.motion_update()
        pf.sense_update(data[i])
        avg_particles[i] = pf.average_particle()
    for i in range(1,  psize-1):
        pf.motion_update()
        avg_particles[i] = pf.average_particle()
        pf.sense_update(avg_particles[i])
    return avg_particles


def pf_prediction(indata, psize):
    pf = ParticleFilter(num_particles=400, motion_model='dynamic')
    for measurement in indata:
        pf.motion_update()
        pf.sense_update(measurement)

    odata = [pf.average_particle()]
    # pf.motion_model = 'dynamic'

    for _ in range(1, psize):
        pf.motion_update()
        average = pf.average_particle()
        odata.append(average)
        pf.sense_update(average)

    return odata

def pf_prediction2(indata, psize):
    pf = ParticleFilter2(indata)
    for index in range(len(indata)):
        pf.update(index)

    odata = []
    for _ in range(0, psize):
        odata.append(pf.estimated_position())
        pf.update()

    return odata


'''
from rdp import rdp
def pf_prediction3(indata, psize):
    # approximate subdivided polygon with Douglas-Peucker algorithm
    appr_traj = rdp(indata, 10)
    i_traj = indata.copy()
    common_points = [np.where((indata == appr_pt).all(axis=1))
                     for appr_pt in appr_traj]
    #(index0,) = common_points[0]
    (index0,) = common_points[0][0]
    #for (index1,) in common_points[1:]:
    for I in common_points[1:]:
        index1 = I[0][0]
        step = (indata[index1] - indata[index0]) / (index1 - index0)
        for i in range(index0, index1):
            i_traj[i+1] = i_traj[i] + step
        index0 = index1

    print("Number of coordinates:", len(indata), len(appr_traj), len(i_traj))
    pf = ParticleFilter2(i_traj)
    for index in range(len(indata)):
        pf.update(index)

    odata = []
    for _ in range(0, psize):
        odata.append(pf.estimated_position())
        pf.update()
    return odata
'''
