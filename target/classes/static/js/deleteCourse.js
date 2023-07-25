let idCourse;
console.log("ok");
$(".deleteCourseButton").click(function () {
    idCourse = $(this).attr("idCourse");
    console.log("...");
    $("#alert-block-course").css("display", "flex");
})
$("#alert-btn-course-cancel").click(function () {
    $("#alert-block-course").css("display", "none");
})
$("#alert-btn-course-confirm").click(function () {
    window.location.href = '/admin/courses/delete/' + idCourse;
})