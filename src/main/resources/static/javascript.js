// const url = window.location.href.substr(22, 5);
// const navElements = document.getElementsByClassName("nav-link");
// console.log(url);
//
// for (let j = 0; j < navElements.length; j++) {
//     navElements[j].className = navElements[j].className.replace(" active", "");
// }
//
// for (let i = 0; i < navElements.length; i++) {
//     if (navElements[i].id.startsWith(url)) {
//
//         if (!navElements[i].classList.contains("active")) {
//             navElements[i].className += " active";
//         }
//     }
// }

const nav = document.getElementById("navBarsExampleDefault")
const navElements = nav.getElementsByTagName("a");
for (let i = 0; i < navElements.length; i++) {

    navElements[i].addEventListener("click", function () {
        let currentActive = nav.getElementsByClassName("active");
        if(currentActive.length > 0) {
            currentActive[0].className = currentActive[0].className.replace(" active", "");
        }
        this.className += " active";
    });
}

