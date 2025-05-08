const screenWidth = window.innerWidth;

if (screenWidth > 1300) {
  let firstCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".first-card",
      start: "60% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  firstCard.to(".first-card", {
    y: -400,
    opacity: 0,
  });

  let firstImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".first-card-img",
      start: "60% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  firstImgCard.to(".first-card-img", {
    y: -700,
    opacity: 0,
  });

  let secondCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".second-card",
      start: "65% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  secondCard.to(".second-card", {
    y: -400,
    opacity: 0,
  });

  let secondImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".second-card-img",
      start: "65% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  secondImgCard.to(".second-card-img", {
    y: -700,
    opacity: 0,
  });

  let thirdCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".third-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  thirdCard.to(".third-card", {
    opacity: 0,
  });

  let thirdImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".third-card-img",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  thirdImgCard.to(".third-card-img", {
    y: -400,
  });

  let fourthCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fourth-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fourthCard.to(".fourth-card", {
    x: -400,
    opacity: 0,
  });

  let fourthImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fourth-card-img",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fourthImgCard.to(".fourth-card-img", {
    opacity: 0,
  });

  let fifthCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fifth-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fifthCard.to(".fifth-card", {
    x: 400,
    opacity: 0,
  });

  let lastRow = gsap.timeline({
    scrollTrigger: {
      trigger: ".fifth-row",
      start: "-20% center",
      end: "center center",
      scrub: true,
      markers: false,
    },
  });

  lastRow.to(".fifth-row", {
    opacity: 1,
  });
}

if (screenWidth < 1300 && screenWidth > 460) {
  let firstCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".first-card",
      start: "65% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  firstCard.to(".first-card", {
    y: -400,
    opacity: 0,
  });

  let firstImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".first-card-img",
      start: "65% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  firstImgCard.to(".first-card-img", {
    y: -700,
    opacity: 0,
  });

  let secondCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".second-card",
      start: "65% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  secondCard.to(".second-card", {
    y: -400,
    opacity: 0,
  });

  let secondImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".second-card-img",
      start: "65% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  secondImgCard.to(".second-card-img", {
    y: -700,
    opacity: 0,
  });

  let thirdCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".third-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  thirdCard.to(".third-card", {
    opacity: 0,
  });

  let thirdImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".third-card-img.second",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  thirdImgCard.to(".third-card-img.second", {
    y: -400,
  });

  let fourthCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fourth-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fourthCard.to(".fourth-card", {
    x: -400,
    opacity: 0,
  });

  let fourthImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fourth-card-img",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fourthImgCard.to(".fourth-card-img", {
    opacity: 0,
  });

  let fifthCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fifth-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fifthCard.to(".fifth-card", {
    x: 400,
    opacity: 0,
  });

  let lastRow = gsap.timeline({
    scrollTrigger: {
      trigger: ".fifth-row",
      start: "-20% center",
      end: "center center",
      scrub: true,
      markers: false,
    },
  });

  lastRow.to(".fifth-row", {
    opacity: 1,
  });
}

if (screenWidth <= 460) {
  let firstCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".first-card",
      start: "100% center",
      end: "170% center",
      scrub: true,
      markers: false,
    },
  });

  firstCard.to(".first-card", {
    y: -300,
    opacity: 0,
  });

  let firstImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".first-card-img",
      start: "65% center",
      end: "100% center",
      scrub: true,
      markers: false,
    },
  });

  firstImgCard.to(".first-card-img", {
    y: -300,
    opacity: 0,
  });

  let secondCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".second-card",
      start: "100% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  secondCard.to(".second-card", {
    y: -400,
    opacity: 0,
  });

  let secondImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".second-card-img",
      start: "65% center",
      end: "120% center",
      scrub: true,
      markers: false,
    },
  });

  secondImgCard.to(".second-card-img", {
    y: -300,
    opacity: 0,
  });

  let thirdCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".third-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  thirdCard.to(".third-card", {
    opacity: 0,
  });

  let thirdImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".third-card-img.second",
      start: "70% center",
      end: "120% center",
      scrub: true,
      markers: false,
    },
  });

  thirdImgCard.to(".third-card-img.second", {
    y: -400,
    opacity: 0,
  });

  let fourthCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fourth-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fourthCard.to(".fourth-card", {
    x: -400,
    opacity: 0,
  });

  let fourthImgCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fourth-card-img",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fourthImgCard.to(".fourth-card-img", {
    opacity: 0,
  });

  let fifthCard = gsap.timeline({
    scrollTrigger: {
      trigger: ".fifth-card",
      start: "75% center",
      end: "140% center",
      scrub: true,
      markers: false,
    },
  });

  fifthCard.to(".fifth-card", {
    x: 400,
    opacity: 0,
  });

  let lastRow = gsap.timeline({
    scrollTrigger: {
      trigger: ".fifth-row",
      start: "-20% center",
      end: "center center",
      scrub: true,
      markers: false,
    },
  });

  lastRow.to(".fifth-row", {
    opacity: 1,
  });
}
