package hu.mycompany.machinemanager.service;

public class AnotherJobIsAlreadyRunningException extends RuntimeException {
    public AnotherJobIsAlreadyRunningException(Long machineId) {
        super(String.format("Another job is already running on machine [%d]", machineId));
    }
}
