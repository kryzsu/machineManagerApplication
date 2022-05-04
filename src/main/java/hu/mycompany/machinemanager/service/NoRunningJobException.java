package hu.mycompany.machinemanager.service;

public class NoRunningJobException extends RuntimeException {
    public NoRunningJobException(Long machineId) {
        super(String.format("No Running job on machine [%d]", machineId));
    }
}
