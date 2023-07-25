let idPost;
console.log("ok");
$(".deleteButton").click(function () {
    idPost = $(this).attr("idPost");
    $("#alert-block").css("display", "flex");
})
$("#alert-btn-cancel").click(function () {
    $("#alert-block").css("display", "none");
})
$("#alert-btn-confirm").click(function () {
    window.location.href = '/admin/posts/delete/' + idPost;
})