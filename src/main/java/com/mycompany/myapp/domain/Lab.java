package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Lab.
 */
@Entity
@Table(name = "lab")
public class Lab implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "descritpion")
    private String descritpion;

    @ManyToMany
    @JoinTable(name = "rel_lab__student", joinColumns = @JoinColumn(name = "lab_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonIgnoreProperties(value = { "labs" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lab id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Lab title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescritpion() {
        return this.descritpion;
    }

    public Lab descritpion(String descritpion) {
        this.setDescritpion(descritpion);
        return this;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Lab students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Lab addStudent(Student student) {
        this.students.add(student);
        student.getLabs().add(this);
        return this;
    }

    public Lab removeStudent(Student student) {
        this.students.remove(student);
        student.getLabs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lab)) {
            return false;
        }
        return id != null && id.equals(((Lab) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lab{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", descritpion='" + getDescritpion() + "'" +
            "}";
    }
}
