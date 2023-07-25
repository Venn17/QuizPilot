const colorList = [
    "rgba(255, 93, 93, 0.59)",
    "rgba(255, 223, 93, 0.59)",
    "rgba(123, 255, 93, 0.59)",
    "rgba(93, 255, 217, 0.59)",
    "rgba(93, 125, 255, 0.59)",
    "rgba(225, 93, 255, 0.59)",
    "rgba(255, 93, 204, 0.59)"
];
let isPauseArrow = false;
let canvas = document.querySelector(".a-lot-of-arrow");
let canvasX = 0;
let canvasY = 0;
canvasX = window.getComputedStyle(canvas).getPropertyValue("width");
canvasY = window.getComputedStyle(canvas).getPropertyValue("height");
canvasX = removePixel(canvasX);
canvasY = removePixel(canvasY);

document.querySelector(".w3-sidebar").style.transition = "0s";
document.querySelector(".w3-sidebar").style.width = "0px";
document.querySelector(".w3-main").style.marginLeft = "0px";
document.querySelector(".answer-group");

function selectAnswerEvent(event) {
    if (event.target.matches(".answer-group")) {
        const elm = event.target;
        let target = elm.getAttribute("target");
        document.getElementById(target).setAttribute("checked", "checked");
        elm.classList.add("answer-active");
        elm.parentElement.parentElement.parentElement.classList.add("question-active");
        let siblings = elm.parentElement.parentElement.children;
        for (let i = 0; i < siblings.length; i++) {
            let siblingElement = siblings[i];
            if (siblingElement.children[0] != elm) {
                siblingElement.children[0].classList.remove("answer-active");
            }
        }
    }
}

function spawnArrow(interval) {
    if (!isPauseArrow) {
        setTimeout(() => makeALotOfArrow(interval), interval);
    } else {
        setTimeout(() => spawnArrow(interval), interval);
    }
}

function makeALotOfArrow(interval) {
    let id = (Math.random() + 1).toString(36).substring(2);
    let yOfArrow = Math.floor(Math.random() * canvasY);
    let fontSize = Math.floor(Math.random() * (100 - 30 + 1)) + 30;
    let arrow = document.createElement("i");
    let color = Math.floor(Math.random() * colorList.length);
    let animationDuration = Math.floor(Math.random() * (15 - 5 + 1)) + 5;
    arrow.setAttribute("id", id);
    arrow.classList.add("fa", "fa-play", "run-from-left-to-right");
    arrow.style.top = yOfArrow + "px";
    arrow.style.left = "-100px";
    arrow.style.fontSize = fontSize + "px";
    arrow.style.color = colorList[color];
    arrow.style.textShadow = "0px 0px 20px " + colorList[color];
    arrow.style.animationDuration = animationDuration + "s";
    spawnArrow(interval);
    pushToCanvas(arrow);
    setTimeout(() => killThisArrow(id), animationDuration * 1000);
}

function killThisArrow(arrowId) {
    document.getElementById(arrowId).remove();

}

function pushToCanvas(arrow) {
    let canvas = document.querySelector(".a-lot-of-arrow");
    canvas.append(arrow);

}

function removePixel(val) {
    return parseInt(val.slice(0, -2));
}

document.getElementById("start-test").addEventListener("click", function () {

})

//override start button
clickStartButton = function () {
    isPauseArrow = true;
    moveSmallArrow();
    // moveBigArrow();
    setTimeout(() => {
        moveCloud();
    }, 300);
    setTimeout(() => {
        isPauseArrow = false;
        if (!isInit) {
            isInit = true;
            document.getElementById("test-info").style.height = "0px";
            document.getElementById("test-info").style.opacity = "0";
        }
    }, 2000);
    setTimeout(() => {
        document.getElementById("test-form").style.display = "block";
        clockPopinPopout();
        coolInitEffect(0, 0, 0);

    }, 4000);
}

function moveSmallArrow() {
    let smallArrow = document.getElementById("start-test");
    smallArrow.style.fontSize = "200px";
    smallArrow.style.transform = "translate(-40%,-50%)";
}

function moveBigArrow() {
    let bigArrow = document.getElementById("really-big-play-button");
    bigArrow.classList.add("bounce-off");
    bigArrow.style.animationDelay = "1s";
}

function moveCloud() {
    cloudStopAnimation();
    moveTitle();
    moveDifficult();
    moveTimeLimit();
    moveTotalQuestion();
}

function moveTitle() {
    let title = document.getElementById("test-title");
    title.style.transition = "2s";
    title.style.left = "0px";
    title.style.top = "0px";
    title.style.opacity = "0";
    setTimeout(() => {
        title.style.display = "none";
    }, 2000);
}

function moveDifficult() {
    let difficult = document.getElementById("difficult");
    difficult.style.transition = "2s";
    difficult.style.left = "0px";
    difficult.style.top = "100%";
    difficult.style.opacity = "0";
    setTimeout(() => {
        difficult.style.display = "none";
    }, 2000);
}

function moveTotalQuestion() {
    let totalQuestion = document.getElementById("total-question");
    totalQuestion.style.transition = "2s";
    totalQuestion.style.left = "100%";
    totalQuestion.style.top = "0%";
    totalQuestion.style.opacity = "0";
    setTimeout(() => {
        totalQuestion.style.display = "none";
    }, 2000);
}

function moveTimeLimit() {
    let duration = document.getElementById("time-limit");
    duration.style.transition = "2s";
    duration.style.left = "100%";
    duration.style.top = "100%";
    duration.style.opacity = "0";
    setTimeout(() => {
        duration.style.display = "none";
    }, 2000);
}

function cloudStopAnimation() {
    let clouds = document.querySelectorAll(".cloud");
    for (let i = 0; i < clouds.length; i++) {
        const cloud = clouds[i];
        stopAnimation(cloud);
    }
}

function stopAnimation(cloud) {
    let circles = cloud.querySelectorAll(".circle");
    for (let i = 0; i < circles.length; i++) {
        const circle = circles[i];
        circle.style.playState = "pause";
    }
}

duration = document.getElementById("test-info").getAttribute("duration");
duration = duration * 60;
let isInit = false;
let timing = 10;
let durationPerSec = duration / timing;
let leap = Math.ceil(durationPerSec / timing);
let intervalPerSecond = 1000 / 60;
let hourPlaceHolder = document.getElementById("hour");
let minutePlaceHolder = document.getElementById("minute");
let secondPlaceHolder = document.getElementById("second");
let hour = Math.floor(duration / (60 * 60));
let minute = Math.floor((duration - (hour * 60 * 60)) / 60);
let second = duration % 60;


function coolInitEffect(currentHour, currentMinute, currentSecond) {
    if (currentHour == hour && currentMinute == minute && currentSecond > second) {
        pushToPlaceHolder(hour, minute, second);
        questionInit();
    } else if (currentHour == hour && currentMinute > minute) {
        questionInit();
        pushToPlaceHolder(hour, minute, second);
    } else {
        currentSecond += leap;
        if (currentSecond > 59) {
            currentSecond = 0;
            currentMinute++;
            if (currentMinute == 60) {
                currentHour++;
                currentMinute = 0;
            }
        }
        if (leap > 10) {
            let randomSecond = Math.floor(Math.random() * (50 - 1 + 1)) + 1;
            pushToPlaceHolder(currentHour, currentMinute, randomSecond);
        } else {
            pushToPlaceHolder(currentHour, currentMinute, currentSecond);
        }
        setTimeout(() => {
            coolInitEffect(currentHour, currentMinute, currentSecond);
        }, intervalPerSecond);
    }

}

function questionInit() {
    let questions = document.querySelectorAll(".question-block");
    for (const question of questions) {
        question.classList.add("question-block-init");
    }
    setTimeout(() => {
        countDownTestTime(duration);
    }, 500 + 1000)
}

function pushToPlaceHolder(hour, minute, second) {
    hourPlaceHolder.querySelector("i").innerText = hour;
    minutePlaceHolder.querySelector("i").innerText = minute;
    secondPlaceHolder.querySelector("i").innerText = second;
}

function assignRainbowColor(itemList) {
    for (let i = 0; i < itemList.length; i++) {
        const item = itemList[i];
        let color = colorList[i];
        item.style.color = color;
    }
}

let rainbowArrow = document.querySelectorAll(".rainbow-train-of-arrow i");

spawnArrow(500);
window.onclick = (event) => {
    selectAnswerEvent(event);
}

window.onresize = function () {
    canvasX = window.getComputedStyle(canvas).getPropertyValue("width");
    canvasY = window.getComputedStyle(canvas).getPropertyValue("height");
    canvasX = removePixel(canvasX);
    canvasY = removePixel(canvasY);
}

document.querySelector(".mua-quat").addEventListener("click", function () {
    this.querySelector(".quat").style.animationDuration = "0.4s";
})


let hidden;
let visibilityChange;
if (typeof document.hidden !== "undefined") { // Opera 12.10 and Firefox 18 and later support
    hidden = "hidden";
    visibilityChange = "visibilitychange";
} else if (typeof document.msHidden !== "undefined") {
    hidden = "msHidden";
    visibilityChange = "msvisibilitychange";
} else if (typeof document.webkitHidden !== "undefined") {
    hidden = "webkitHidden";
    visibilityChange = "webkitvisibilitychange";
}

function onVisibilityChange() {
    if (document["hidden"]) {
        isPauseArrow = true;
    } else {
        isPauseArrow = false;
    }
}

document.addEventListener(visibilityChange, onVisibilityChange, false);

function hoverSubmitIn() {
    for (let i = 0; i < rainbowArrow.length; i++) {
        const arrow = rainbowArrow[i];
        arrow.classList.add("submit-hovering");
    }
}

function hoverSubmitOut() {
    for (let i = 0; i < rainbowArrow.length; i++) {
        const arrow = rainbowArrow[i];
        arrow.classList.remove("submit-hovering");
    }
}