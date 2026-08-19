package com.example.mafunzo.Utils;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.Module;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    private CourseRepository() {
        // Classe utilitaire
    }

    public static List<Course> getCourses() {

        List<Course> courses = new ArrayList<>();

        // ==========================================
        // JAVA
        // ==========================================

        Course java = new Course(
                "C001",
                "Java pour débutants",
                "Informatique",
                "Apprenez les bases de Java et de la programmation orientée objet.",
                "MAFUNZO Academy",
                30
        );

        java.addModule(new Module(
                "M001",
                "Introduction à Java",
                "Découvrez Java et son fonctionnement.",
                Module.TYPE_PDF,
                "introduction_java.pdf"
        ));

        java.addModule(new Module(
                "M002",
                "Variables et types",
                "Apprenez à déclarer et utiliser les variables.",
                Module.TYPE_PDF,
                "variables_java.pdf"
        ));

        java.addModule(new Module(
                "M003",
                "Conditions",
                "Apprenez à utiliser if, else et switch.",
                Module.TYPE_VIDEO,
                "java_conditions"
        ));

        java.addModule(new Module(
                "M004",
                "Quiz Java",
                "Vérifiez vos connaissances sur les bases de Java.",
                Module.TYPE_QUIZ,
                "quiz_java"
        ));

        // ==========================================
        // HTML / CSS
        // ==========================================

        Course web = new Course(
                "C002",
                "HTML & CSS",
                "Développement Web",
                "Apprenez à créer des pages web modernes.",
                "MAFUNZO Academy",
                20
        );

        web.addModule(new Module(
                "M005",
                "Introduction au Web",
                "Comprenez le fonctionnement du Web.",
                Module.TYPE_PDF,
                "introduction_web.pdf"
        ));

        web.addModule(new Module(
                "M006",
                "HTML",
                "Structurez vos pages avec HTML.",
                Module.TYPE_PDF,
                "html.pdf"
        ));

        web.addModule(new Module(
                "M007",
                "CSS",
                "Stylisez vos interfaces avec CSS.",
                Module.TYPE_PDF,
                "css.pdf"
        ));

        // ==========================================
        // MARKETING
        // ==========================================

        Course marketing = new Course(
                "C003",
                "Marketing digital",
                "Marketing",
                "Découvrez les fondamentaux du marketing numérique.",
                "MAFUNZO Academy",
                15
        );

        marketing.addModule(new Module(
                "M008",
                "Introduction au marketing",
                "Découvrez les bases du marketing digital.",
                Module.TYPE_PDF,
                "marketing_intro.pdf"
        ));

        marketing.addModule(new Module(
                "M009",
                "Réseaux sociaux",
                "Découvrez comment utiliser les réseaux sociaux pour développer une activité.",
                Module.TYPE_PDF,
                "reseaux_sociaux.pdf"
        ));

        courses.add(java);
        courses.add(web);
        courses.add(marketing);

        return courses;
    }

    public static Course getCourseById(String courseId) {

        for (Course course : getCourses()) {

            if (course.getId().equals(courseId)) {
                return course;
            }
        }

        return null;
    }
}
