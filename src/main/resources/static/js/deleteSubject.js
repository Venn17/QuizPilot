let target, idButton;
console.log("ok sub");
$(".deleteButton").click(function () {
    target = $(this).attr("target");
    idButton = $(this).attr("idButton");
    $("#alert-block").css("display", "flex");
})
$("#alert-btn-cancel").click(function () {
    $("#alert-block").css("display", "none");
})
$("#alert-btn-confirm").click(function () {
    if (target == "subject") {
        window.location.href = '/admin/subjects/delete/' + idButton;
    } else {
        window.location.href = '/admin/courses/delete/' + idButton;
    }

})