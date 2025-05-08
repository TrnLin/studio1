const screenWidth = window.innerWidth;

//header
if (screenWidth > 1024) {
  let heroMain = gsap.timeline({
    scrollTrigger: {
      trigger: ".hero-main",
      start: "65% 50%",
      end: "-40% -100%",
      scrub: true,
      markers: false,
    },
  });

  //hero-sub
  let heroSub = gsap.timeline({
    scrollTrigger: {
      trigger: ".hero-sub",
      start: "-65% 40%",
      end: "90% 0%",
      scrub: true,
      markers: false,
    },
  });

  //about
  let tl = gsap.timeline({
    scrollTrigger: {
      trigger: ".about-container",
      start: "-90% 90%",
      end: "50% 60%",
      scrub: true,
      markers: false,
    },
  });

  //contribute
  let contributeRight = gsap.timeline({
    scrollTrigger: {
      trigger: ".right",
      start: "-20% center",
      end: "50% center",
      scrub: true,
      markers: false,
    },
  });

  let contributeLeft = gsap.timeline({
    scrollTrigger: {
      trigger: ".left",
      start: "-20% center",
      end: "50% center",
      scrub: true,
      markers: false,
    },
  });

  //data analytics
  let dataAnalytics = gsap.timeline({
    scrollTrigger: {
      trigger: ".fourth-container",
      start: "-80% center",
      end: "50% center",
      scrub: true,
      markers: false,
    },
  });

  tl.to(".about-container", {
    x: 800,
    opacity: 1,
  });

  heroMain.to(".hero-main", {
    y: -600,
    opacity: 0,
  });

  heroSub.to(".hero-sub", {
    x: 700,
    opacity: 0,
  });

  contributeLeft.to(".left", {
    x: 800,
    opacity: 1,
  });

  contributeRight.to(".right", {
    x: -800,
    opacity: 1,
  });

  dataAnalytics.to(".fourth-container", {
    opacity: 1,
  });
}

//mobile
if (screenWidth < 450) {
  let heroMain = gsap.timeline({
    scrollTrigger: {
      trigger: ".hero-main",
      start: "160% 50%",
      end: "300% -100%",
      scrub: true,
      markers: false,
    },
  });

  //hero-sub
  let heroSub = gsap.timeline({
    scrollTrigger: {
      trigger: ".hero-sub",
      start: "-65% 40%",
      end: "90% 0%",
      scrub: true,
      markers: false,
    },
  });

  //about
  let tl = gsap.timeline({
    scrollTrigger: {
      trigger: ".about-container",
      start: "-90% 90%",
      end: "50% 60%",
      scrub: true,
      markers: false,
    },
  });

  //contribute
  let contributeRight = gsap.timeline({
    scrollTrigger: {
      trigger: ".right",
      start: "-50% center",
      end: "top center",
      scrub: true,
      markers: false,
    },
  });

  let contributeLeft = gsap.timeline({
    scrollTrigger: {
      trigger: ".left",
      start: "-50% center",
      end: "70% center",
      scrub: true,
      markers: false,
    },
  });

  //data analytics
  let dataAnalytics = gsap.timeline({
    scrollTrigger: {
      trigger: ".fourth-container",
      start: "-120% center",
      end: "50% center",
      scrub: true,
      markers: false,
    },
  });

  tl.to(".about-container", {
    x: 800,
    opacity: 1,
  });

  heroMain.to(".hero-main", {
    y: -300,
    opacity: 0,
  });

  heroSub.to(".hero-sub", {
    x: 700,
    opacity: 0,
  });

  contributeLeft.to(".left", {
    x: 400,
    opacity: 1,
  });

  contributeRight.to(".right", {
    x: -400,
    opacity: 1,
  });

  dataAnalytics.to(".fourth-container", {
    opacity: 1,
  });
}
function easterEgg1() {
  var quote = document.querySelector(".easter-container");

  quote.style.display = "flex";
}

function easterEgg() {
  var squid = document.querySelector(".easter-egg-2");

  squid.style.display = "block";
}
