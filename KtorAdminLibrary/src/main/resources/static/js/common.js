function hasExpandedClassInSidebar() {
    const icon = document.getElementById("menu-expand-icon");
    return icon.classList.contains("expanded")
}

function handleMenuHover() {
    const menu = document.querySelector('.menu');
    const sidebar = document.querySelector('.sidebar');

    let isSidebarVisible = false;

    menu.addEventListener('mouseover', function () {
        if (!isSidebarVisible) {
            sidebar.style.left = '0';
            isSidebarVisible = true;
        }
    });

    sidebar.addEventListener('mouseover', function () {
        if (!isSidebarVisible) {
            sidebar.style.left = '0';
            isSidebarVisible = true;
        }
    });

    document.body.addEventListener('mouseover', function (event) {
        if (!sidebar.contains(event.target) && !menu.contains(event.target) && !hasExpandedClassInSidebar()) {
            sidebar.style.left = '-300px';
            isSidebarVisible = false;
        }
    });
}

function handleTitleClicks() {
    let titles = document.getElementsByClassName("title")
    for (let title of titles) {
        title.style.cursor = "pointer"
        title.onclick = function () {
            window.location.href = "/admin"
        }
    }
}

const expandedSidebarValue = "expanded"
const themeKey = "theme"


function changeTheme() {
    const sidebar = document.getElementsByClassName("sidebar")[0];
    if (localStorage.getItem(themeKey) === "dark") {
        localStorage.setItem(themeKey, "light")
        document.querySelector(":root").classList.remove("theme-dark")
    } else {
        localStorage.setItem(themeKey, "dark")
        document.querySelector(":root").classList.add("theme-dark")
    }
    sidebar.style.backgroundColor = getCSSVariable("--white-transparent-60")
    try {
        handleRichInputs()
    } catch (_) {
    }
}

function getDefaultTheme() {
    return window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? "dark" : "light"
}

function initTheme() {
    let allNodes = document.querySelectorAll("*")
    allNodes.forEach(node => node.classList.add("no-animation"));
    let theme = localStorage.getItem(themeKey) ?? getDefaultTheme();
    if (theme === "dark") {
        document.querySelector(":root").classList.add("theme-dark")
    }
    allNodes.forEach(node => node.classList.remove("no-animation"));
}

function getCSSVariable(variableName) {
    return getComputedStyle(document.documentElement).getPropertyValue(variableName).trim();
}

function handleSidebarExpandedFromStorage() {
    if (localStorage.getItem("sidebarExpanded") === expandedSidebarValue) {
        const sidebar = document.getElementsByClassName("sidebar")[0];
        const icon = document.getElementById("menu-expand-icon");
        const container = document.getElementById("container");
        const menuIcon = document.getElementsByClassName("menu")[0];
        sidebar.classList.add("no-animation")
        container.classList.add("no-animation")
        icon.classList.add("no-animation")
        menuIcon.classList.add("no-animation")
        sidebar.style.left = '0';
        expandSidebar(icon, container, sidebar, menuIcon)
        setTimeout(
            () => {
                sidebar.classList.remove("no-animation")
                container.classList.remove("no-animation")
                icon.classList.remove("no-animation")
                menuIcon.classList.remove("no-animation")
            }, 500
        )
    }
}


function runInitialFunctions() {
    initTheme();
    handleSidebarExpandedFromStorage();
}

document.addEventListener('DOMContentLoaded', function () {
    handleMenuHover()
    handleTitleClicks()
    runInitialFunctions()
});

window.addEventListener("pageshow", runInitialFunctions);


function expandOrShrinkSidebar() {
    const icon = document.getElementById("menu-expand-icon");
    const container = document.getElementById("container");
    const sidebar = document.getElementsByClassName("sidebar")[0];
    const menuIcon = document.getElementsByClassName("menu")[0];
    if (icon.classList.contains("expanded")) {
        icon.classList.remove("expanded");
        container.style.width = "100%";
        container.style.marginLeft = "0";
        localStorage.removeItem("sidebarExpanded")
        menuIcon.classList.remove("shrink");
        sidebar.style.backgroundColor = getCSSVariable("--white-transparent-70")
        sidebar.style.backdropFilter = "blur(8px)"
        sidebar.style.border = "1px solid hsla(213, 10%, 18%, 0.1)"
    } else {
        localStorage.setItem("sidebarExpanded", expandedSidebarValue)
        expandSidebar(icon, container, sidebar, menuIcon)
    }
}

function expandSidebar(icon, container, sidebar, menuIcon) {
    let sidebarRect = sidebar.getBoundingClientRect();
    icon.classList.add("expanded");
    menuIcon.classList.add("shrink");
    container.style.marginLeft = (16 + sidebarRect.width).toString() + "px";
    container.style.width = `calc(100vw - ${sidebarRect.width + 48}px)`;
    sidebar.style.backgroundColor = getCSSVariable("--white-transparent-60")
    sidebar.style.backdropFilter = "none"
    sidebar.style.border = "none"
}


function openPanel(pluralName) {
    window.location.href = `/admin/${pluralName}`
}

function openDashboard() {
    window.location.href = `/admin`
}


function logout() {
    const loading = document.getElementById("loading");
    loading.style.visibility = "visible";
    const options = {
        method: "POST",
        body: null,
    }
    fetch("/admin/logout", options).then(
        async response => {
            if (response.ok) {
                window.location.replace("/admin/login")
            } else {
                loading.style.visibility = "hidden";
                alert(`ERROR`)
            }
        }
    ).catch(error => {
        console.log(error.message)
    }).finally(() => {
        loading.style.visibility = "hidden";
    })
}