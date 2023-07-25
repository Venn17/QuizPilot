const domain = window.location.origin;
let page = 1;
let perPage = 5;
let search = "";
let orderBy = "id";
let orderRule = "DESC";
let loadPage = document.body.getAttribute("load");
let ajax = new XMLHttpRequest();
//abstract function for alert confirm button
let confirmAlertButtonFunction = () => {
};

//
if (loadPage == "question") {
    sendSearchQuestionRequest();
}
//admin change subject;
let subjectSelector = document.getElementById("subject");
if (subjectSelector != null) {
    activeCourse(subjectSelector.value);
    setCourseValue();
    subjectSelector.addEventListener("change", function () {
        let subjectId = this.value;
        activeCourse(subjectId);
        setCourseValue();
    })
}

function activeCourse(subjectId) {
    let course = document.getElementById("course-of-" + subjectId);
    let allCourse = document.querySelectorAll(".course-selector");
    for (const courseChild of allCourse) {
        courseChild.classList.replace("w3-show", "w3-hide");
    }
    course.classList.replace("w3-hide", "w3-show");
}

function changeSubjectFilter(element) {
    document.getElementById("subjectFilter").value = element.value;
    let allCourse = document.querySelectorAll(".course-selector");
    for (const courseChild of allCourse) {
        courseChild.value = courseChild.firstElementChild.getAttribute("value");
    }
}

//admin change course value
let allCourse = document.querySelectorAll(".course-selector");
if (allCourse.length != 0) {
    for (const courseChild of allCourse) {
        courseChild.addEventListener("change", function () {
            setCourseValue();
        })
    }
}

function setCourseValue() {
    let activeCourse = document.querySelector(".course-selector.w3-show");
    let courseInput = document.getElementById("course");
    courseInput.value = activeCourse.value;
}

//admin add answer
let add_more_answer_element = document.getElementById("add-more-answer");
if (add_more_answer_element != null) {
    add_more_answer_element.addEventListener("click", function () {
        let noOfAnswer = document.getElementById("answer-field").children.length;
        let divTO = document.createElement("div");
        let randomId = (Math.random() + 1).toString(36).substring(7);
        divTO.setAttribute("class", "w3-row w3-section");
        divTO.setAttribute("id", randomId);
        let theHtml = `
                <div class="w3-col m8">
                    <input type="text" class="w3-input w3-border" placeholder="Answer" name="answers" id="answer${(noOfAnswer + 1)}">
                </div>
                <div class="w3-col m3">
                    <input type="radio" class="w3-radio" name="correctAnswer" value="${(noOfAnswer + 1)}">
                </div>
                <div class="w3-col m1">
                    <button class="w3-button w3-red remove-answer-btn" removeIndex="${randomId}" type="button">
                        <i class="fa fa-times remove-answer-btn" aria-hidden="true"></i>
                    </button>
                </div>`;
        divTO.innerHTML = theHtml;
        document.getElementById("answer-field").appendChild(divTO);
    })
}
//admin remove answer
function activateRemoveBtn(event) {
    if (event.target.matches("i.remove-answer-btn")) {
        if (document.getElementById("answer-field").children.length > 4) {
            let removeIndex = event.target.parentElement.getAttribute("removeIndex");
            document.getElementById(removeIndex).remove();
        }
    }
    if (event.target.matches(".remove-answer-btn")) {
        if (document.getElementById("answer-field").children.length > 4) {
            let removeIndex = event.target.getAttribute("removeIndex");
            document.getElementById(removeIndex).remove();
        }
    }
}

clickOnElementToRedirect();
// click on element to redirect
function clickOnElementToRedirect() {
    let redirectElement = document.querySelectorAll(".redirect-to");
    if (redirectElement != null) {
        for (let i = 0; i < redirectElement.length; i++) {
            const element = redirectElement[i];
            element.addEventListener("click", function () {
                let redirPath = element.getAttribute("href");
                window.location.href = domain + redirPath;
            })
        }
    }
}

function toggleOrderRule() {
    let orderRuleList = document.getElementById("order-rule-list");
    let isShow = orderRuleList.classList.contains("w3-show");
    if (isShow) {
        orderRuleList.classList.remove("w3-show");
    } else {
        orderRuleList.classList.add("w3-show");
    }
}

function changeOrderRule(event) {
    if (event.target.matches(".order-by-btn")) {
        let orderRuleText = event.target.innerText;
        orderRule = event.target.getAttribute("rule");
        page = 1;
        document.getElementById("current-order-rule").innerText = orderRuleText;
        document.getElementById("order-rule-list").classList.remove("w3-show");
        sendSearchQuestionRequest();
    }
}

//admin search question send request
let searchBtn = document.getElementById("search-btn");
if (searchBtn != null) {
    searchBtn.addEventListener("click", function () {
        page = 1;
        sendSearchQuestionRequest();
    })
}

function sendSearchQuestionRequest() {
    let subject = document.getElementById("subjectFilter").value;
    let course = document.getElementById("course").value;
    let level = document.getElementById("level").value;
    search = document.getElementById("search-value").value;
    let urlParam = makeGetParam("page", page)
        + "&" + makeGetParam("search", search)
        + "&" + makeGetParam("orderBy", orderBy)
        + "&" + makeGetParam("orderRule", orderRule)
        + "&" + makeGetParam("perPage", perPage)
        + "&" + makeGetParam("subject", subject)
        + "&" + makeGetParam("course", course)
        + "&" + makeGetParam("level", level);
    ajax.open("GET", domain + "/api/question/search?" + urlParam);
    ajax.send();
    ajax.onreadystatechange = () => {
        handleSearchResponse()
    };
}

function makeGetParam(key, param) {
    return key + "=" + encodeURIComponent(param);
}

function handleSearchResponse() {
    if (ajax.readyState == 4) {
        let result = JSON.parse(ajax.response)["results"];
        let questions = result.questions;
        let html = ``;
        for (let i = 0; i < questions.length; i++) {
            const question = questions[i];
            html += renderQuestionBlock(question);
        }
        $("#quest-table-body").html(html);
        clickOnElementToRedirect();
        renderNewPaginateBlock(result.totalQuestion);
    }
}

function renderQuestionBlock(question) {
    return `<tr class="hov-darken cur-pointer redirect-to w3-border-bottom" href="/admin/question/edit/${question.id}">
                <td>${question.text}</td>
                <td>${question.course.name}</td>
                <td>${question.level}</td>
                <td>
                    ${renderAnswerBlock(question.answerList)}
                </td>
            </tr>`;
}

function renderAnswerBlock(answerList) {
    let html = ``;
    for (let i = 0; i < answerList.length; i++) {
        const answer = answerList[i];
        html += `
            <div class="">
                <div class="w3-col m6 w3-border w3-padding-small ${(answer.correct) ? "w3-green" : ""} ">${answer.text}</div>
            </div>
        `;
    }
    return html;
}

function renderNewPaginateBlock(totalQuestion) {
    let pageJump = 3;
    let maxPage = totalQuestion / perPage;
    let startRender = ((page - pageJump) < 1) ? 1 : (page - pageJump);
    let endRender = ((parseInt(page) + pageJump) > maxPage) ? maxPage : (parseInt(page) + pageJump);
    let parentPaginateBlock = document.getElementById("page-selection");
    let button = document.createElement("button");
    let dotdotdot = document.createElement("p");
    dotdotdot.innerText = "...";
    dotdotdot.classList.add("w3-btn-spacing", "margin-0");
    parentPaginateBlock.innerHTML = "";
    button.classList.add("w3-button", "page-select-button");
    if (startRender > 1) {
        button.innerText = "1";
        button.setAttribute("value", "1");
        parentPaginateBlock.innerHTML += button.outerHTML;
        parentPaginateBlock.innerHTML += dotdotdot.outerHTML;
    }
    for (let i = startRender - 1; i < endRender; i++) {
        button.setAttribute("value", "" + (i + 1));
        button.classList.remove("w3-black");
        button.innerText = "" + (i + 1);
        if (i == (page - 1)) {
            button.classList.add("w3-black");
        }
        parentPaginateBlock.innerHTML += button.outerHTML;
    }
    if (endRender < maxPage) {
        parentPaginateBlock.innerHTML += dotdotdot.outerHTML;
        button.innerText = "" + maxPage;
        button.setAttribute("value", "" + maxPage);
        parentPaginateBlock.innerHTML += button.outerHTML;
    }

}


function clickOnPageSelectEvent(event) {
    if (event.target.matches(".page-select-button")) {
        page = event.target.getAttribute("value");
        sendSearchQuestionRequest();
    }
}


//delete question alert
let deleteBtn = document.getElementById("delete-btn");
if (deleteBtn != null) {
    deleteBtn.addEventListener("click", function () {
        document.getElementById("alert-block").style.display = "flex";
        confirmAlertButtonFunction = submitDelete;
    })
}
// cancel delete
let cancelAlertBtn = document.querySelector("#alert-btn-cancel");
if (cancelAlertBtn != null) {
    cancelAlertBtn.addEventListener("click", function () {
        document.getElementById("alert-block").style.display = "none";
    })
}
// confirm delete button
let confirmButton = document.getElementById("alert-btn-confirm");
if (confirmButton != null) {
    confirmButton.addEventListener("click", function () {
        confirmAlertButtonFunction();
    })
}

function submitDelete() {
    let id = document.getElementById("id").value;
    let requestBody = {"id": id};
    ajax.open("DELETE", domain + "/api/question");
    ajax.setRequestHeader("Content-Type", "application/json");
    ajax.send(JSON.stringify(requestBody));
    ajax.onreadystatechange = () => {
        handleDeleteResponse()
    };
}

// response if it's possible to delete, if not render block of test using this question
function handleDeleteResponse() {
    if (ajax.readyState == 4) {
        let result = JSON.parse(ajax.response)["results"];
        if (result.deleteStatus == 1) {
            window.location.href = domain + "/admin/question/home";
        } else {
            document.querySelector(".alert-info").innerText = "This question is being used by";
            let testList = result.testList;
            let alertDetail = document.querySelector(".alert-detail");
            alertDetail.innerHTML = "";
            for (let i = 0; i < testList.length; i++) {
                let test = testList[i];
                let aTag = document.createElement("a");
                aTag.href = domain + "/admin/test/edit/" + test.id;
                aTag.style.display = "block";
                aTag.innerText = `${i + 1}\t.${test.text}`;
                alertDetail.appendChild(aTag);
            }
        }
    }
}

let isNotSubmitted = true;
let counterIntervalId;
let clockState = "relax";
let duration;
const counter = document.getElementById("counter");
// hide test info , display questions , start count down
let startTestButton = document.getElementById("start-test");
if (startTestButton != null) {
    startTestButton.addEventListener("click", function () {
        clickStartButton();
    })
    confirmAlertButtonFunction = function () {
        document.getElementById("alert-block").style.display = "none";
    }
    //clock on clock to show or hide time remain detail
    document.querySelector("#cute-clock i").addEventListener("click", function () {
        let currentRightPosition = document.getElementById("counter").style.right;
        if (currentRightPosition == "0px" || currentRightPosition == "") {
            counter.style.right = "-270px";
        } else {
            counter.style.right = "0px";
        }
    })

}

function clickStartButton() {
    duration = document.getElementById("test-info").getAttribute("duration");
    // minute to second
    duration = duration * 60;
    document.getElementById("test-info").style.display = "none";
    document.getElementById("test-form").style.display = "block";
    countDownTestTime(duration);
    //hide clock after 5s
    setTimeout(function () {
        counter.style.right = "-270px";
    }, 5000);
}

function countDownTestTime(timeLeft) {
    let hour = Math.floor(timeLeft / (60 * 60));
    let minute = Math.floor(timeLeft / 60) - (hour * 60);
    let second = timeLeft % 60;
    document.querySelector("#counter #time-detail #hour i").textContent = hour;
    document.querySelector("#counter #time-detail #minute i").textContent = minute;
    document.querySelector("#counter #time-detail #second i").textContent = second;
    if (timeLeft == 0) {
        doSubmitAnswer();
        document.getElementById("alert-block").querySelector(".alert-header").innerHTML = `<h3>Time out!</h3>`;
    }
    if (timeLeft > 0) {
        timeLeft--;
        counterIntervalId = setTimeout(() => countDownTestTime(timeLeft--), 1000);
    }
    if (timeLeft < (duration / 3) && clockState == "relax") {
        counter.classList.remove("w3-green");
        counter.classList.add("w3-amber");
        counter.querySelector("#cute-clock").classList.remove("w3-text-green");
        counter.querySelector("#cute-clock").classList.add("w3-text-amber");
        clockState = "hurry";
        clockPopinPopout();
    }
    if (timeLeft < (duration / 4) && (clockState == "relax" || clockState == "hurry")) {
        counter.classList.remove("w3-amber");
        counter.classList.add("w3-red");
        counter.querySelector("#cute-clock").classList.remove("w3-text-amber");
        counter.querySelector("#cute-clock").classList.add("w3-text-red");
        clockState = "last-spurs";
        clockPopinPopout();
    }
}

function clockPopinPopout() {
    counter.style.right = "0px";
    setTimeout(function () {
        counter.style.right = "-270px";
    }, 5000);
}

// submit answer event
let submitAnswerButton = document.getElementById("submit-answer");
if (submitAnswerButton != null) {
    submitAnswerButton.addEventListener("click", function () {
        doSubmitAnswer();
    })
}


let userToken = getCookie("token");

//submit answer
function doSubmitAnswer() {
    const testId = document.getElementById("testId").value;
    let answers = document.querySelectorAll(".answer-input:checked");
    let answerValue = [];
    let index = 0;
    let token = userToken;
    clearInterval(counterIntervalId);
    answers.forEach(answer => {
        let questionId = answer.getAttribute("name");
        questionId = questionId.slice(9, questionId.length);
        let selectedAnswer = answer.getAttribute("value");
        answerValue[index] = {questionId, selectedAnswer};
        index++;
    })
    let requestBody = {testId, answerValue, token};
    if (isNotSubmitted) {
        ajax.open("POST", domain + "/api/test/submit");
        ajax.setRequestHeader("Content-Type", "application/json");
        ajax.send(JSON.stringify(requestBody));
        ajax.onreadystatechange = () => {
            testSubmitResponseHandler()
        };
        isNotSubmitted = false;
    }
    document.getElementById("submit-answer").style.display = "none";
}

function testSubmitResponseHandler() {
    if (ajax.readyState == 4 && ajax.status == 200) {
        let result = JSON.parse(ajax.response)["results"];
        let totalScore = document.getElementById("test-info").getAttribute("totalScore");
        document.getElementById("alert-block").style.display = "flex";
        document.querySelector("#alert-block .alert-info").innerHTML = `You scored ${result.score}/${totalScore}`;
        displayResult(result.resultList);
    }
}

function displayResult(resultList) {
    let checkedIcon = `<span><i class="fa fa-check w3-text-green" aria-hidden="true"></i></span>`;
    let incorrectIcon = `<span><i class="fa fa-times w3-text-red" aria-hidden="true"></i></span>`;
    for (let i = 0; i < resultList.length; i++) {
        const result = resultList[i];
        let isCorrect = (result.selectedAnswer == result.correctAnswer) ? true : false;
        let selectedInput = document
            .querySelector(".answer-input[name='question-" + result.questionId + "'][value='" + result.selectedAnswer + "']");
        let correctInput = document
            .querySelector(".answer-input[name='question-" + result.questionId + "'][value='" + result.correctAnswer + "']");
        if (isCorrect) {
            selectedInput.parentElement.classList.replace("answer-active", "answer-active-correct");
        } else {
            selectedInput.parentElement.classList.replace("answer-active", "answer-active-wrong");
            correctInput.parentElement.style.background = "#53BF9D";
        }
    }

}


window.onclick = function (event) {
    clickOnPageSelectEvent(event);
    changeOrderRule(event);
    activateRemoveBtn(event);
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}