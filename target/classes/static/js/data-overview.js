//Country-World
const select1 = document.getElementsByClassName("select-selected")[0];

select1.setAttribute("onclick", "change()");

var worldCountryOption = document.getElementById("world-country-option").value;
const stateCityCard = document.querySelector(".city-state-container");
const availCountry = document.querySelector(".avai-container");
const formSetupWorld = document.querySelector("#world-search-bar");
const formSetupCountry = document.querySelector("#country-search-bar");
const additionalInfo = document.querySelector(".add-info-container");

if (worldCountryOption == "world") {
  availCountry.style.display = "none";
  stateCityCard.style.display = "none";
  formSetupWorld.style.display = "flex";
  formSetupCountry.style.display = "none";
  additionalInfo.style.display = "none";
} else {
  stateCityCard.style.display = "flex";
  availCountry.style.display = "flex";
  formSetupWorld.style.display = "none";
  formSetupCountry.style.display = "flex";
  additionalInfo.style.display = "flex";
}

function change() {
  var worldCountryOption = document.getElementById(
    "world-country-option"
  ).value;
  const stateCityCard = document.querySelector(".city-state-container");
  const availCountry = document.querySelector(".avai-container");
  const formSetupWorld = document.querySelector("#world-search-bar");
  const formSetupCountry = document.querySelector("#country-search-bar");
  const additionalInfo = document.querySelector(".add-info-container");

  if (worldCountryOption == "world") {
    availCountry.style.display = "none";
    stateCityCard.style.display = "none";
    formSetupWorld.style.display = "flex";
    formSetupCountry.style.display = "none";
    additionalInfo.style.display = "none";
  } else if (worldCountryOption == "country") {
    stateCityCard.style.display = "flex";
    availCountry.style.display = "flex";
    formSetupWorld.style.display = "none";
    formSetupCountry.style.display = "flex";
    additionalInfo.style.display = "flex";
  }
}

//default value

const startYear = document.querySelector("#start-year");
const endYear = document.querySelector("#end-year");
const countryStartYear = document.querySelector("#country-start-year");
const countryEndYear = document.querySelector("#country-end-year");

//ChartJS Handler
const ctx1 = document.getElementById("population");
const ctx2 = document.getElementById("temperature");
const countryInput = document.querySelector("#country-input");

function makeChart() {
  var worldCountryOption = document.getElementById(
    "world-country-option"
  ).value;
  if (worldCountryOption == "world") {
    var temDom = "/chartData/WorldOverview/Temperature";
    var popDom = "/chartData/WorldOverview/Population";
    var posBody =
      "startYear=" +
      encodeURIComponent(startYear.value) +
      "&endYear=" +
      encodeURIComponent(endYear.value);
  } else {
    var temDom = "/chartData/CountryOverview/Temperature";
    var popDom = "/chartData/CountryOverview/Population";
    var posBody =
      "startYear=" +
      encodeURIComponent(countryStartYear.value) +
      "&endYear=" +
      encodeURIComponent(countryEndYear.value) +
      "&country=" +
      encodeURIComponent(countryInput.value);
  }

  //Population chart
  fetch(popDom, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: posBody,
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((jsonData) => {
      //This create population chart
      new Chart(ctx1, {
        type: "bar",
        data: {
          labels: jsonData.Year,
          datasets: [
            {
              label: "",
              data: jsonData.Population,
              borderWidth: 1,
              backgroundColor: "#F9B572",
              borderColor: "#F9B572",
            },
          ],
        },
        options: {
          scales: {
            y: {
              beginAtZero: true,
            },
            x: {
              grid: {
                display: false,
              },
            },
          },
          plugins: {
            legend: {
              display: false,
            },
          },
          maintainAspectRatio: false,
        },
      });
    })
    .catch((error) => {
      console.error("Fetch error:", error);
    });

  //Temperature chart chart
  fetch(temDom, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: posBody,
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((jsonData) => {
      //This create temperature chart
      new Chart(ctx2, {
        type: "line",
        data: {
          labels: jsonData.Year,
          datasets: [
            {
              label: "",
              data: jsonData.Temp,
              borderWidth: 1,
              backgroundColor: "#F9B572",
              borderColor: "#F9B572",
            },
          ],
        },
        options: {
          scales: {
            y: {
              beginAtZero: true,
              grid: {
                display: false,
              },
            },
          },
          plugins: {
            legend: {
              display: false,
            },
          },
          maintainAspectRatio: false,
        },
      });
    })
    .catch((error) => {
      console.error("Fetch error:", error);
    });
}

window.addEventListener("change", makeChart());
// ================================================================================================

startYear.value = null;
endYear.value = null;
countryStartYear.value = null;
countryEndYear.value = null;

//alert

function validate() {
  var startYear = document.getElementById("start-year");
  var endYear = document.getElementById("end-year");
  var countryStartYear = document.getElementById("country-start-year");
  var countryEndYear = document.getElementById("country-end-year");
  var input = document.getElementById("country-input");

  var countryValid = input.validity;
  var startYearValid = startYear.validity;
  var endYearValid = endYear.validity;
  var countryStartYearValid = countryStartYear.validity;
  var countryEndYearValid = countryEndYear.validity;

  if (
    countryValid.valueMissing ||
    startYearValid.valueMissing ||
    endYearValid.valueMissing ||
    countryStartYearValid.valueMissing ||
    countryEndYearValid.valueMissing
  ) {
    const alert = document.querySelector(".alert");
    alert.style.display = "flex";
    setTimeout(() => {
      const alert = document.querySelector(".alert");
      alert.style.display = "none";

      // 👇️ hides element (still takes up space on page)
      // box.style.visibility = 'hidden';
    }, 3000);
  }

  const countryStartYearValue = countryStartYear.value;
  const countryEndYearValue = countryEndYear.value;

  if (countryStartYearValue < 1750 || countryEndYearValue > 2013) {
    const alert = document.querySelector(".alert");
    const alertContent = document.querySelector(".alert-content");
    alertContent.innerHTML = "Please enter a year between 1750 and 2013";
    alert.style.display = "flex";
    setTimeout(() => {
      const alert = document.querySelector(".alert");
      alert.style.display = "none";

      // 👇️ hides element (still takes up space on page)
      // box.style.visibility = 'hidden';
    }, 3000);
  }
  input.reportValidity();
}

//Countries table sorting and filtering

document
  .getElementsByClassName("select-selected")[1]
  .setAttribute("onclick", "ascDescOrder()");

document
  .getElementsByClassName("select-selected")[2]
  .setAttribute("onclick", "ascDescOrder()");

const orderCountries = document.querySelector("#order-countries").value;
const popuTempOrder = document.querySelector("#popu-temp-order").value;

if (orderCountries == "dsc") {
  if (popuTempOrder == "popu") {
    const dscPopu = document.querySelector("#available-country-popu-desc");
    const dscTemp = document.querySelector("#available-country-temp-desc");
    const ascPopu = document.querySelector("#available-country-popu-asc");
    const ascTemp = document.querySelector("#available-country-temp-asc");

    dscPopu.style.display = "table";
    dscTemp.style.display = "none";
    ascPopu.style.display = "none";
    ascTemp.style.display = "none";
  } else if (popuTempOrder == "temp") {
    const dscPopu = document.querySelector("#available-country-popu-desc");
    const dscTemp = document.querySelector("#available-country-temp-desc");
    const ascPopu = document.querySelector("#available-country-popu-asc");
    const ascTemp = document.querySelector("#available-country-temp-asc");

    dscPopu.style.display = "none";
    dscTemp.style.display = "table";
    ascPopu.style.display = "none";
    ascTemp.style.display = "none";
  }
} else if (orderCountries == "asc") {
  if (popuTempOrder == "popu") {
    const dscPopu = document.querySelector("#available-country-popu-desc");
    const dscTemp = document.querySelector("#available-country-temp-desc");
    const ascPopu = document.querySelector("#available-country-popu-asc");
    const ascTemp = document.querySelector("#available-country-temp-asc");

    dscPopu.style.display = "none";
    dscTemp.style.display = "none";
    ascPopu.style.display = "table";
    ascTemp.style.display = "none";
  } else if (popuTempOrder == "temp") {
    const dscPopu = document.querySelector("#available-country-popu-desc");
    const dscTemp = document.querySelector("#available-country-temp-desc");
    const ascPopu = document.querySelector("#available-country-popu-asc");
    const ascTemp = document.querySelector("#available-country-temp-asc");

    dscPopu.style.display = "none";
    dscTemp.style.display = "none";
    ascPopu.style.display = "none";
    ascTemp.style.display = "table";
  }
}
function ascDescOrder() {
  const orderCountries = document.querySelector("#order-countries").value;
  const popuTempOrder = document.querySelector("#popu-temp-order").value;

  if (orderCountries == "dsc") {
    if (popuTempOrder == "popu") {
      const dscPopu = document.querySelector("#available-country-popu-desc");
      const dscTemp = document.querySelector("#available-country-temp-desc");
      const ascPopu = document.querySelector("#available-country-popu-asc");
      const ascTemp = document.querySelector("#available-country-temp-asc");

      dscPopu.style.display = "table";
      dscTemp.style.display = "none";
      ascPopu.style.display = "none";
      ascTemp.style.display = "none";
    } else if (popuTempOrder == "temp") {
      const dscPopu = document.querySelector("#available-country-popu-desc");
      const dscTemp = document.querySelector("#available-country-temp-desc");
      const ascPopu = document.querySelector("#available-country-popu-asc");
      const ascTemp = document.querySelector("#available-country-temp-asc");

      dscPopu.style.display = "none";
      dscTemp.style.display = "table";
      ascPopu.style.display = "none";
      ascTemp.style.display = "none";
    }
  } else if (orderCountries == "asc") {
    if (popuTempOrder == "popu") {
      const dscPopu = document.querySelector("#available-country-popu-desc");
      const dscTemp = document.querySelector("#available-country-temp-desc");
      const ascPopu = document.querySelector("#available-country-popu-asc");
      const ascTemp = document.querySelector("#available-country-temp-asc");

      dscPopu.style.display = "none";
      dscTemp.style.display = "none";
      ascPopu.style.display = "table";
      ascTemp.style.display = "none";
    } else if (popuTempOrder == "temp") {
      const dscPopu = document.querySelector("#available-country-popu-desc");
      const dscTemp = document.querySelector("#available-country-temp-desc");
      const ascPopu = document.querySelector("#available-country-popu-asc");
      const ascTemp = document.querySelector("#available-country-temp-asc");

      dscPopu.style.display = "none";
      dscTemp.style.display = "none";
      ascPopu.style.display = "none";
      ascTemp.style.display = "table";
    }
  }
}

//City state table soring and filtering

document
  .getElementsByClassName("select-selected")[3]
  .setAttribute("onclick", "cityState()");

document
  .getElementsByClassName("select-selected")[4]
  .setAttribute("onclick", "cityState()");

document
  .getElementsByClassName("select-selected")[5]
  .setAttribute("onclick", "cityState()");

const hideDefault = document.querySelectorAll(".list-container table");

hideDefault.forEach((hideDefault) => {
  hideDefault.style.display = "none";
});

// const defaultCityState = document.querySelector("#state-avg-asc");
const defaultCityState = document.querySelector("#city-avg-asc");
defaultCityState.style.display = "table";

const defaultTempPopu = document.querySelector("#available-country-popu-desc");
defaultTempPopu.style.display = "table";

function cityState() {
  const cityStateOtion = document.querySelector("#city-state").value;
  const cityStateSort = document.querySelector("#city-state-sorting").value;
  const minMaxAvg = document.querySelector("#max-min-avg-sorting").value;

  if (cityStateOtion == "city") {
    const city = document.querySelectorAll(".city");
    const state = document.querySelectorAll(".state");

    city.forEach((city) => {
      city.style.display = "table";
    });
    state.forEach((state) => {
      state.style.display = "none";
    });

    if (cityStateSort == "asc") {
      const asc = document.querySelectorAll(".city.asc");
      const dsc = document.querySelectorAll(".city.desc");

      asc.forEach((asc) => {
        asc.style.display = "table";
      });
      dsc.forEach((dsc) => {
        dsc.style.display = "none";
      });

      if (minMaxAvg == "min") {
        const min = document.querySelector(".city.asc.min");
        const max = document.querySelector(".city.asc.max");
        const avg = document.querySelector(".city.asc.avg");

        min.style.display = "table";
        max.style.display = "none";
        avg.style.display = "none";
      } else if (minMaxAvg == "max") {
        const min = document.querySelector(".city.asc.min");
        const max = document.querySelector(".city.asc.max");
        const avg = document.querySelector(".city.asc.avg");

        min.style.display = "none";
        max.style.display = "table";
        avg.style.display = "none";
      } else if (minMaxAvg == "avg") {
        const min = document.querySelector(".city.asc.min");
        const max = document.querySelector(".city.asc.max");
        const avg = document.querySelector(".city.asc.avg");

        min.style.display = "none";
        max.style.display = "none";
        avg.style.display = "table";
      }
    } else if (cityStateSort == "dsc") {
      const asc = document.querySelectorAll(".city.asc");
      const dsc = document.querySelectorAll(".city.desc");

      asc.forEach((asc) => {
        asc.style.display = "none";
      });
      dsc.forEach((dsc) => {
        dsc.style.display = "table";
      });

      if (minMaxAvg == "min") {
        const min = document.querySelector(".city.desc.min");
        const max = document.querySelector(".city.desc.max");
        const avg = document.querySelector(".city.desc.avg");

        min.style.display = "table";
        max.style.display = "none";
        avg.style.display = "none";
      } else if (minMaxAvg == "max") {
        const min = document.querySelector(".city.desc.min");
        const max = document.querySelector(".city.desc.max");
        const avg = document.querySelector(".city.desc.avg");

        min.style.display = "none";
        max.style.display = "table";
        avg.style.display = "none";
      } else if (minMaxAvg == "avg") {
        const min = document.querySelector(".city.desc.min");
        const max = document.querySelector(".city.desc.max");
        const avg = document.querySelector(".city.desc.avg");

        min.style.display = "none";
        max.style.display = "none";
        avg.style.display = "table";
      }
    }
  } else if (cityStateOtion == "state") {
    const city = document.querySelectorAll(".city");
    const state = document.querySelectorAll(".state");

    city.forEach((city) => {
      city.style.display = "none";
    });
    state.forEach((state) => {
      state.style.display = "table";
    });

    if (cityStateSort == "asc") {
      const asc = document.querySelectorAll(".state.asc");
      const dsc = document.querySelectorAll(".state.desc");

      asc.forEach((asc) => {
        asc.style.display = "table";
      });
      dsc.forEach((dsc) => {
        dsc.style.display = "none";
      });

      if (minMaxAvg == "min") {
        const min = document.querySelector(".state.asc.min");
        const max = document.querySelector(".state.asc.max");
        const avg = document.querySelector(".state.asc.avg");

        min.style.display = "table";
        max.style.display = "none";
        avg.style.display = "none";
      } else if (minMaxAvg == "max") {
        const min = document.querySelector(".state.asc.min");
        const max = document.querySelector(".state.asc.max");
        const avg = document.querySelector(".state.asc.avg");

        min.style.display = "none";
        max.style.display = "table";
        avg.style.display = "none";
      } else if (minMaxAvg == "avg") {
        const min = document.querySelector(".state.asc.min");
        const max = document.querySelector(".state.asc.max");
        const avg = document.querySelector(".state.asc.avg");

        min.style.display = "none";
        max.style.display = "none";
        avg.style.display = "table";
      }
    } else if (cityStateSort == "dsc") {
      const asc = document.querySelectorAll(".state.asc");
      const dsc = document.querySelectorAll(".state.desc");

      asc.forEach((asc) => {
        asc.style.display = "none";
      });
      dsc.forEach((dsc) => {
        dsc.style.display = "table";
      });

      if (minMaxAvg == "min") {
        const min = document.querySelector(".state.desc.min");
        const max = document.querySelector(".state.desc.max");
        const avg = document.querySelector(".state.desc.avg");

        min.style.display = "table";
        max.style.display = "none";
        avg.style.display = "none";
      } else if (minMaxAvg == "max") {
        const min = document.querySelector(".state.desc.min");
        const max = document.querySelector(".state.desc.max");
        const avg = document.querySelector(".state.desc.avg");

        min.style.display = "none";
        max.style.display = "table";
        avg.style.display = "none";
      } else if (minMaxAvg == "avg") {
        const min = document.querySelector(".state.desc.min");
        const max = document.querySelector(".state.desc.max");
        const avg = document.querySelector(".state.desc.avg");

        min.style.display = "none";
        max.style.display = "none";
        avg.style.display = "table";
      }
    }
  }
}
