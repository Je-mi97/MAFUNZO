package com.example.mafunzo.Utils;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.Module;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    private CourseRepository() {
    }

    public static List<Course> getCourses() {

        List<Course> courses =
                new ArrayList<>();

        // JAVA — GRATUIT

        Course java =
                new Course(
                        "C001",
                        "Java pour débutants",
                        "Informatique",
                        "Apprenez les bases de Java et de la programmation.",
                        "MAFUNZO Academy",
                        0
                );

        java.addModule(
                new Module(
                        "M001",
                        "Introduction à Java",
                        "Découvrez Java et son fonctionnement.",
                        Module.TYPE_PDF,
                        "introduction_java.pdf",
                        25
                )
        );

        java.addModule(
                new Module(
                        "M002",
                        "Variables et types",
                        "Apprenez à déclarer et utiliser les variables.",
                        Module.TYPE_PDF,
                        "variables_java.pdf",
                        30
                )
        );

        java.addModule(
                new Module(
                        "M003",
                        "Conditions",
                        "Apprenez à utiliser if, else et switch.",
                        Module.TYPE_VIDEO,
                        "https://www.youtube.com/watch?v=_vjgGTwWmso",
                        10
                )
        );

        java.addModule(
                new Module(
                        "M004",
                        "Quiz Java",
                        "Vérifiez vos connaissances sur les bases de Java.",
                        Module.TYPE_QUIZ,
                        "quiz_java",
                        3
                )
        );

        // HTML & CSS — PAYANT

        Course web =
                new Course(
                        "C002",
                        "HTML & CSS",
                        "Développement Web",
                        "Apprenez à créer et mettre en forme des pages Web.",
                        "MAFUNZO Academy",
                        10000
                );

        web.addModule(
                new Module(
                        "M005",
                        "Introduction au Web",
                        "Découvrez le fonctionnement du Web.",
                        Module.TYPE_PDF,
                        "introduction_web.pdf",
                        20
                )
        );

        web.addModule(
                new Module(
                        "M006",
                        "HTML",
                        "Apprenez à structurer une page Web avec HTML.",
                        Module.TYPE_VIDEO,
                        "https://www.youtube.com/watch?v=8FqZZrbnwkM",
                        30
                )
        );

        web.addModule(
                new Module(
                        "M007",
                        "CSS",
                        "Apprenez à mettre en forme une page Web avec CSS.",
                        Module.TYPE_VIDEO,
                        "https://www.youtube.com/watch?v=HN4-7k0zC-Y",
                        30
                )
        );

        web.addModule(
                new Module(
                        "M010",
                        "Quiz HTML & CSS",
                        "Vérifiez vos connaissances.",
                        Module.TYPE_QUIZ,
                        "quiz_html_css",
                        3
                )
        );

        // MARKETING DIGITAL — PAYANT

        Course marketing =
                new Course(
                        "C003",
                        "Marketing digital",
                        "Marketing",
                        "Découvrez les fondamentaux du marketing numérique.",
                        "MAFUNZO Academy",
                        8000
                );

        marketing.addModule(
                new Module(
                        "M008",
                        "Introduction au marketing",
                        "Découvrez les bases du marketing digital.",
                        Module.TYPE_PDF,
                        "marketing_intro.pdf",
                        25
                )
        );

        marketing.addModule(
                new Module(
                        "M009",
                        "Réseaux sociaux",
                        "Découvrez le marketing sur les réseaux sociaux.",
                        Module.TYPE_VIDEO,
                        "https://www.bpifrance-universite.fr/formation/marketing-digital-reprenez-les-bases-utilisez-les-reseaux-sociaux-a-bon-escient/",
                        13
                )
        );

        marketing.addModule(
                new Module(
                        "M011",
                        "Quiz Marketing",
                        "Vérifiez vos connaissances.",
                        Module.TYPE_QUIZ,
                        "quiz_marketing",
                        3
                )
        );

        // AJOUT DES FORMATIONS

        courses.add(java);
        courses.add(web);
        courses.add(marketing);

        return courses;
    }


    public static Course getCourseById(
            String courseId
    ) {

        for (Course course :
                getCourses()) {

            if (course.getId()
                    .equals(courseId)) {

                return course;
            }
        }

        return null;
    }

}
