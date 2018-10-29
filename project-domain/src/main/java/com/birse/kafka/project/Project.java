package com.birse.kafka.project;

public class Project {

    private String name;
    private boolean submitted;
    private long id;

    public Project() {}


    public Project(String name, boolean submitted, long id) {
        this.name = name;
        this.submitted = submitted;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", submitted=" + submitted +
                ", id=" + id +
                '}';
    }
}
