#!/usr/bin/env python

""" TODO Module description """

import numpy as np


def cross(v1, v2):
    """ Why is np.cross three times as slow? """
    return v1[0] * v2[1] - v1[1] * v2[0]


def intersect_segment_circle(seg_start, seg_end, center, radius):
    """
    >>> b = np.array([0, -9])
    >>> e = np.array([0,  9])
    >>> c = np.array([0,  0])
    >>> r = 4.0
    >>> list(intersect_segment_circle(b, e, c, r))
    [0.0, -4.0] 
    """
    seg_direction = seg_end - seg_start
    mu = intersect_ray_circle(seg_start, seg_direction, center, radius)
    if mu is None:
        return None
    return seg_start + mu * seg_direction


def intersect_ray_circle(seg_start, seg_dir, center, radius):
    """
    http://codereview.stackexchange.com/questions/86421/line-segment-to-circle-collision-algorithm
    """
    P = seg_start  # Start of line segment
    D = seg_dir    # Vector along line segment
    C = center
    r = radius

    a = D.dot(D)
    b = 2 * D.dot(P - C)
    c = P.dot(P) + C.dot(C) - 2 * P.dot(C) - r**2
    discriminant = b**2 - 4 * a * c

    if discriminant < 0:
        # If the discriminant is negative, then the line misses the circle entirely.
        return None

    sqrt_disc = np.sqrt(discriminant)
    t1 = (-b + sqrt_disc) / (2 * a)
    t2 = (-b - sqrt_disc) / (2 * a)
    solutions = [t for t in (t1, t2) if (0 <= t <= 1)]

    if not solutions:
        # If neither of these is between 0 and 1,
        # then the line segment misses the circle (but would hit it if extended)
        return None

    return min(solutions)


def intersect_seg_seg(b1, e1, b2, e2):
    """
    Return intersection between two segments from b1 to e1, and from b2 to e2

    >>> b1 = np.array([1,  0])
    >>> e1 = np.array([-1,  0])
    >>> b2 = np.array([0,  1])
    >>> e2 = np.array([0, -1])
    >>> list(intersect_seg_seg(b1, e1, b2, e2))
    [0.0, 0.0]
    """
    d1 = e1 - b1
    mu = intersect_ray_segment(b1, d1, b2, e2)
    if mu is None:
        return None
    return b1 + mu * d1


def intersect_ray_segment(b1, d1, b2, e2, epsilon=1.0E-9):
    """
    Return intersection between two segments from b1 to e1, and from b2 to e2
    """
    d2 = e2 - b2
    det = cross(d1, d2)
    if abs(det) < epsilon:  # if determinant is 0, lines are parallel.  No intersection
        return None

    mu = cross((b2 - b1), d2) / float(det)
    if mu < 0 or mu > 1.0:
        return None

    return mu
