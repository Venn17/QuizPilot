$(".course-btn").click(function () {
    $(this).attr("target");
    let target = $(this).attr("target");
    $("#" + target).css("display", "block").siblings().css("display", "none");
})

function clearFilter() {
    window.location = '/admin/test';
}