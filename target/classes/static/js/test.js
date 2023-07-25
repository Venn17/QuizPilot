let ajax = new XMLHttpRequest();
const domain = window.location.origin;

// list subject
let subjectSelector
$('.subject-selector').change(function () {
    let subjectId = this.value;
    activeCourse(subjectId);
    setCourseValue();
})
activeCourse($('.subject-selector').val());

// list course
function setCourseValue() {
    let activeCourse = $(".course-selector.w3-show");
    let courseInput = $("#course");
    courseInput.val(activeCourse.val());
}

function activeCourse(subjectId) {
    let allCourse = $(".course-selector")
    let course = $("#course-" + subjectId);
    for (let i = 0; i < allCourse.length; i++) {
        const courseChild = allCourse[i];
        $(courseChild).removeClass("w3-show").addClass("w3-hide");
    }
    $(course).removeClass("w3-hide").addClass("w3-show");
}

$('.course-selector').change(function () {
    setCourseValue();
})

function clearFilter() {
    window.location = '/admin/test';
}

//check delete
let deleteId;

function deleteClick(elem) {
    deleteId = $(elem).attr("href");
    $("#alert-block").css("display", "flex");
}

function deleteClickCancel() {
    $("#alert-block").css("display", "none");
}

function deleteClickConfirm() {
    window.location = deleteId;
}
//filter
let subjectSearch = "";
let courseSearch = "";
let page = 1;
let search = "";

$("#filter").submit(function (event) {
    event.preventDefault();
    subjectSearch = $(this).find("#subjectSearch").val();
    courseSearch = $(this).find("#courseSearch").val();
    search = $(this).find("#keyword").val();
    sendSearchTestRequest();
})

function sendSearchTestRequest() {
    let urlParam = makeGetParam("keyword", search)
        + "&" + makeGetParam("subjectSearch", subjectSearch)
        + "&" + makeGetParam("courseSearch", courseSearch)
        + "&" + makeGetParam("currentPage", page);
    ajax.open("GET", domain + "/api/test/filter?" + urlParam);
    ajax.send();
    ajax.onreadystatechange = () => {
        handleSearchResponse()
    };
}

function makeGetParam(key, param) {
    return key + "=" + encodeURIComponent(param);
}

function handleSearchResponse() {
    if (ajax.readyState == 4 && ajax.status == 200) {
        let result = JSON.parse(ajax.response);
        let testList = result.testList;
        let totalPage = result.totalPage;
        if (testList.length == 0) {
            return filterError();
        }
        $('#table > table').css('display', 'table');
        $('#page').css('display', 'block');
        $('#errorSearch').css('display', 'none');
        renderTable(testList);
        renderPaginate(page, totalPage);
        activePageBtn();
    }
}

function renderTable(testList) {
    let tbody = '';
    testList.forEach(test => {
        tbody += makeTestRow(test);
    });
    $("#tbody").html(tbody);
}

function makeTestRow(test) {
    return `<tr>
                <td>${test.text}</td>
                <td>${test.course.subject.name}</td>
                <td>${test.course.name}</td>
                <td>${test.level}</td>
                <td>${test.duration}</td>
                <td>${test.mark}</td>
                <th class="w3-large">
                    <a href="/admin/test/edit/${test.id}"><i class="fa fa-pencil"></i></a> |
                    <button class="cur-pointer delete_btn" style="background: none; border: none;" onclick="deleteClick(this)"
                          href="/admin/test/delete/${test.id}"><i class="fa fa-trash"></i></button>
                </th>
            </tr>`
}

function renderPaginate(currentPage, totalPage) {
    let threeDot = `<span className="w3-button" id="dotdotdotleft">
                    ...
                </span>`
    let page = ``;
    let sequence = 2;
    let start = (currentPage - sequence < 1) ? '1' : currentPage - sequence;
    let end = (currentPage + sequence > totalPage) ? totalPage : currentPage + sequence;
    page += firstPage(currentPage);
    if ((currentPage - sequence) > 1) {
        page += threeDot;
    }
    page += makePageSelection(start, end, currentPage);
    if ((currentPage + sequence) < totalPage) {
        page += threeDot;
    }
    page += lastPage(currentPage, totalPage);
    $('#page').html(page);
}

function firstPage(currentPage) {
    let first = `<span class="pageButton" value="1" id="first-btn">
                    <a class="w3-button">First</a>
                </span>
                <span class="pageButton" value="${currentPage - 1}" id="pre-btn">
                    <a class="w3-button">&laquo;</a>
                </span>`
    return (currentPage > 1) ? first : '';
}

function lastPage(currentPage, totalPage) {
    let last = `<span class="pageButton" value="${parseInt(currentPage) + 1}" id="next-btn">
                    <a class="w3-button">&raquo;</a>
                </span>
                <span class="pageButton" value="${totalPage}" id="last-btn">
                    <a class="w3-button">Last</a>
                </span>`
    return (currentPage != totalPage) ? last : '';
}

function makePageSelection(start, end, currentPage) {
    let isCurrentPage = ``;
    let pageButton = "";
    for (let i = start; i <= end; i++) {
        if (currentPage == i) {
            isCurrentPage = "w3-green";
        } else {
            isCurrentPage = "";
        }
        pageButton += `<span class="pageButton" value="${i}" id="pageSelect">
                            <a class="w3-button ${isCurrentPage}" >${i}</a>
                       </span>`
    }
    return pageButton;
}

function activePageBtn() {
    $('.pageButton').click(function () {
        page = $(this).attr("value");
        sendSearchTestRequest();
    })
}

function filterError() {
    $('#table > table').css('display', 'none');
    $('#page').css('display', 'none');
    $('#errorSearch').css('display', 'block');
}

sendSearchTestRequest();

