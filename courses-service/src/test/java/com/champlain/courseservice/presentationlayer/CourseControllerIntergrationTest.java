package com.champlain.courseservice.presentationlayer;


//@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
//@AutoConfigureWebTestClient
class CourseControllerIntergrationTest {
    /*

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CourseRepository courseRepository;

    private String validCourseId;

    private final Long dbSize = 4L;

    @BeforeEach
    public void dbSetup(){
        Course course1 = buildCourse("", "");
        Course course2 = buildCourse("", "");
        Course course3 = buildCourse("", "");
        Course course4 = buildCourse("", "");

        Publisher<Course> setup = courseRepository.deleteAll()
                .thenMany(courseRepository.save(course1))
                .thenMany(courseRepository.save(course2))
                .thenMany(courseRepository.save(course3))
                .thenMany(courseRepository.save(course4));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getCourseByCourseIdString_withValidCourseId(){

        Mono<Course> courseMono = Mono.from(courseRepository.findAll()
                .doOnNext(course -> {
                    validCourseId = course.getCourseId();
                    System.out.println(validCourseId);
                }));

        StepVerifier.create(courseMono)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient
                .get()
                .uri("/courses/{courseId}", validCourseId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.courseId").isEqualTo(validCourseId);
    }

@Test
    void getCourseByCourseIdString_withInValid_throwsNotFoundException(){

        String invalidCourseId = "123";

        webTestClient
                .get()
                .uri("/courses/{courseId}", invalidCourseId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("No student with this studentId was found: " + invalidCourseId);
}



@Test
    void addCourse(){
        CourseRequestDTO newCourseDTO = CourseRequestDTO.builder()
                .courseName("IOS- Mobile Application")
                .courseNumber("403")
                .numHours(40)
                .numCredits(2.00)
                .department("Comp-Sck")
                .build();
}



*/

}