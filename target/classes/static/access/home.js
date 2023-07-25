const acc = document.getElementsByClassName("accordion");

for (let i = 0; i < acc.length; i++) {
    acc[i].addEventListener("click", function () {
        this.classList.toggle("active");
        const panel = this.nextElementSibling;
        if (panel.style.maxHeight) {
            panel.style.maxHeight = null;
        } else {
            panel.style.maxHeight = panel.scrollHeight + "px";
        }
    });
}

function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function (e) {
    if (!e.target.matches('.dropbtn')) {
        const myDropdown = document.getElementById("myDropdown");
        if (myDropdown.classList.contains('show')) {
            myDropdown.classList.remove('show');
        }
    }
}
$('#sort_post').change(function (){
    let sort = $(this).find(':selected').val();
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/sort?sort=' + sort,
        success: function (res){
            console.log("res la"+res);
            $('#view_post').html(res);
        }
    })
});
$('#sort_blog').change(function (){
    let sort = $(this).find(':selected').val();
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/blog/sort?sort=' + sort,
        success: function (res){
            $('#view_blog').html(res);
        }
    })
})