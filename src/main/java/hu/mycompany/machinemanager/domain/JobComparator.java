package hu.mycompany.machinemanager.domain;

import java.util.Comparator;

public class JobComparator implements Comparator<Job> {

    @Override
    public int compare(Job o1, Job o2) {
        if (o1.getPriority() == null) {
            return 1;
        }

        return o1.getPriority().compareTo(o2.getPriority());
    }
}
