package hu.mycompany.machinemanager.service.util;

import java.util.Comparator;

public class IntervalComparator implements Comparator<Interval> {

    @Override
    public int compare(Interval o1, Interval o2) {
        return o1.getStart().compareTo(o2.getStart());
    }
}
