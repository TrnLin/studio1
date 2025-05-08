const ctx4 = document.getElementById("main-graph");
const regionInput = document.getElementById("region-input");
const regionSelected = document.getElementById("region-option");
const startYearInput = document.getElementById("starting-year-input");
const periodInput = document.getElementById("period-year-input");
const fromRange = document.getElementById("from-range");
const toRange = document.getElementById("to-range");

//dropdown variable
const select3 = document.getElementsByClassName("select-selected")[2];
const select4 = document.getElementsByClassName("select-selected")[3];
const select5 = document.getElementsByClassName("select-selected")[4];
const select8 = document.getElementsByClassName("select-selected")[7];

//region filter variable
const filterMainFill = document.querySelector("#filter-main-fill").value;
const withTemp = document.querySelectorAll(".filter.with-temp");
const withPopu = document.querySelectorAll(".filter.with-popu");

const withTempAsc = document.querySelectorAll(".filter.with-temp.asc");
const withTempDsc = document.querySelectorAll(".filter.with-temp.dsc");
const withPopuAsc = document.querySelectorAll(".filter.with-popu.asc");
const withPopuDsc = document.querySelectorAll(".filter.with-popu.dsc");

//each table from region filter
const withTempAscPopu = document.querySelector(".filter.with-temp.asc.popu");
const withTempAscTemp = document.querySelector(".filter.with-temp.asc.temp");

const withTempDscPopu = document.querySelector(".filter.with-temp.dsc.popu");
const withTempDscTemp = document.querySelector(".filter.with-temp.dsc.temp");

const withPopuAscTemp = document.querySelector(".filter.with-popu.asc.temp");
const withPopuAscPopu = document.querySelector(".filter.with-popu.asc.popu");

const withPopuDscTemp = document.querySelector(".filter.with-popu.dsc.temp");
const withPopuDscPopu = document.querySelector(".filter.with-popu.dsc.popu");

//similar country
const similarityCheck = document.getElementById("enableSim");
const similarContainer = document.querySelector(".similarity-container");

//similar value
const absRelOption = document.getElementById("abs-rel-option").value;
const mSimLsimOption = document.getElementById("m-sim-l-sim-option").value;
const simPopuTempOption = document.getElementById("sim-popu-temp-option").value;

//similar table

//default value

window.addEventListener("change", updateChart());

function updateChart() {
  dom = "/chartData/WorldAdvance";
  if (regionSelected.value == "global") {
    var posBody = "region=World";
  } else {
    var posBody = "region=" + encodeURIComponent(regionInput.value);
  }
  posBody +=
    "&startYear=" +
    encodeURIComponent(startYearInput.value) +
    "&period=" +
    encodeURIComponent(periodInput.value) +
    "&fromRange=" +
    encodeURIComponent(fromRange.value) +
    "&toRange=" +
    encodeURIComponent(toRange.value) +
    "&type=" +
    encodeURIComponent(regionSelected.value);
  console.log(posBody);
  fetch(dom, {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: posBody,
  })
    .then((res) => {
      if (res.ok) {
        return res.json();
      } else {
        throw new Error("Something went wrong");
      }
    })
    .then((jsonData) => {
      // Create chart from here
      const chart = new Chart(ctx4, {
        type: "line",
        data: {
          labels: jsonData.Year,
          datasets: [],
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
              display: true,
            },
          },
          maintainAspectRatio: false,
          responsive: true,
        },
      });

      // Create array for 10 colors
      const colorArray = [
        "#EFC65C",
        "#FDCB5A",
        "#FEBF59",
        "#FDB35A",
        "#FC9A5B",
        "#FB7F5C",
        "#FA645D",
        "#F94A5E",
        "#F8305F",
        "#F71660",
      ];
      // This update chartJS data upto 10 datasets
      for (let i = 1; i <= 10; i++) {
        // If there is no data, break the loop
        if (Object.keys(jsonData)[i] == undefined) break;
        // On default, we will push onemore dataset to chartJS
        chart.data.datasets.push({
          label: Object.keys(jsonData)[i],
          data: Object.values(jsonData)[i],
          backgroundColor: colorArray[i],
          borderColor: colorArray[i],
        });
      }
      chart.update();

      // End of creating chart
    })
    .catch((err) => {
      console.log(err);
    });
}

//global option

const regionInputBox = document.querySelector(".country-name-input");
const regionOption = document.getElementById("region-option").value;

if (regionOption == "global") {
  regionInputBox.style.display = "none";
} else {
  regionInputBox.style.display = "flex";
}

const select1 = document.getElementsByClassName("select-selected")[0];

select1.setAttribute("onclick", "change()");

const temperatureDefault = document.querySelector(".temperature-option-show");
const customSelect3 = document.getElementsByClassName("custom-select")[2];
const cityStateSimTable = document.querySelectorAll(".city-state");
const countrySimTable = document.querySelectorAll(".country");

if (regionOption == "city" || regionOption == "state") {
  const x = document.getElementById("filter-main-fill");
  x.value = "temp";

  customSelect3.style.display = "none";
  temperatureDefault.style.display = "flex";
  select3.style.display = "none";
  cityStateSimTable.forEach((item) => {
    item.style.display = "table";
  });
  countrySimTable.forEach((item) => {
    item.style.display = "none";
  });
  select8.style.display = "none";
} else {
  select3.style.display = "flex";
  temperatureDefault.style.display = "none";
  customSelect3.style.display = "flex";
  cityStateSimTable.forEach((item) => {
    item.style.display = "none";
  });
  countrySimTable.forEach((item) => {
    item.style.display = "table";
  });
  select8.style.display = "flex";
}

if (regionOption != "global") {
  regionInput.setAttribute("required", "");
} else {
  regionInput.removeAttribute("required");
}
function change() {
  const regionOption = document.getElementById("region-option").value;
  if (regionOption == "global") {
    regionInputBox.style.display = "none";
  } else {
    regionInputBox.style.display = "flex";
  }

  if (regionOption == "city" || regionOption == "state") {
    const x = document.getElementById("filter-main-fill");
    x.value = "temp";

    customSelect3.style.display = "none";
    temperatureDefault.style.display = "flex";
    select3.style.display = "none";
    cityStateSimTable.forEach((item) => {
      item.style.display = "table";
    });
    countrySimTable.forEach((item) => {
      item.style.display = "none";
    });
    select8.style.display = "none";
  } else {
    select3.style.display = "flex";
    temperatureDefault.style.display = "none";
    customSelect3.style.display = "flex";
    cityStateSimTable.forEach((item) => {
      item.style.display = "none";
    });
    countrySimTable.forEach((item) => {
      item.style.display = "table";
    });
    select8.style.display = "flex";
  }

  if (regionOption != "global") {
    regionInput.setAttribute("required", "");
  } else {
    regionInput.removeAttribute("required");
  }
}

//filter bar
const filterBar = document.querySelector(".filter-bar");
const filterOption = document.getElementById("filter-option").value;
const regionFilterContainer = document.querySelector(
  ".region-filter-container"
);

if (filterOption == "nofilter") {
  filterBar.style.display = "none";
  regionFilterContainer.style.display = "none";
} else {
  filterBar.style.display = "flex";
  regionFilterContainer.style.display = "flex";
}

const select2 = document.getElementsByClassName("select-selected")[1];

select2.setAttribute("onclick", "filter()");

function filter() {
  const filterOption = document.getElementById("filter-option").value;
  const regionFilterContainer = document.querySelector(
    ".region-filter-container"
  );

  if (filterOption == "nofilter") {
    filterBar.style.display = "none";
    regionFilterContainer.style.display = "none";
  } else {
    filterBar.style.display = "flex";
    regionFilterContainer.style.display = "flex";
  }
}

//average difference

const select9 = document.getElementsByClassName("select-selected")[8];

const diffDcs = document.querySelector(".diff-dcs");
const diffAsc = document.querySelector(".diff-acs");
const diff = document.querySelector(".diff");
const sortDiff = document.getElementById("diffAvgSorting").value;

diff.style.display = "table";
diffAsc.style.display = "none";
diffDcs.style.display = "none";

select9.setAttribute("onclick", "sortingDiff()");

function sortingDiff() {
  const sortDiff = document.getElementById("diffAvgSorting").value;

  if (sortDiff == "asc") {
    diffDcs.style.display = "none";
    diffAsc.style.display = "table";
    diff.style.display = "none";
    console.log("asc");
  } else if (sortDiff == "dsc") {
    diffAsc.style.display = "none";
    diffDcs.style.display = "table";
    console.log("dsc");
    diff.style.display = "none";
  }
}

//Region filter

withTemp.forEach((item) => {
  item.style.display = "none";
});
withPopu.forEach((item) => {
  item.style.display = "none";
});

withPopuAscPopu.style.display = "table";

select3.setAttribute("onclick", "popuTempFilter()");
select4.setAttribute("onclick", "popuTempFilter()");
select5.setAttribute("onclick", "popuTempFilter()");

function popuTempFilter() {
  const filterMainFill = document.querySelector("#filter-main-fill").value;
  const filterSortingInput = document.querySelector(
    "#region-filter-sorting"
  ).value;
  const filterOptionInput = document.querySelector(
    "#region-filter-popu-temp"
  ).value;

  if (filterMainFill == "popu") {
    withTemp.forEach((item) => {
      item.style.display = "none";
    });
    withPopu.forEach((item) => {
      item.style.display = "table";
    });

    if (filterSortingInput == "asc") {
      withPopuAsc.forEach((item) => {
        item.style.display = "table";
      });
      withPopuDsc.forEach((item) => {
        item.style.display = "none";
      });

      if (filterOptionInput == "temp") {
        withPopuAscTemp.style.display = "table";
        withPopuAscPopu.style.display = "none";
        console.log("temp");
      } else if (filterOptionInput == "popu") {
        withPopuAscTemp.style.display = "none";
        withPopuAscPopu.style.display = "table";
      }
    } else if (filterSortingInput == "dsc") {
      withPopuAsc.forEach((item) => {
        item.style.display = "none";
      });
      withPopuDsc.forEach((item) => {
        item.style.display = "table";
      });

      if (filterOptionInput == "temp") {
        withPopuDscTemp.style.display = "table";
        withPopuDscPopu.style.display = "none";
      } else if (filterOptionInput == "popu") {
        withPopuDscTemp.style.display = "none";
        withPopuDscPopu.style.display = "table";
      }
    }
  } else if (filterMainFill == "temp") {
    withPopu.forEach((item) => {
      item.style.display = "none";
    });
    withTemp.forEach((item) => {
      item.style.display = "table";
    });

    if (filterSortingInput == "asc") {
      withTempAsc.forEach((item) => {
        item.style.display = "table";
      });
      withTempDsc.forEach((item) => {
        item.style.display = "none";
      });

      if (filterOptionInput == "temp") {
        withTempAscTemp.display = "table";
        withTempAscPopu.display = "none";
      } else if (filterOptionInput == "popu") {
        withTempAscTemp.display = "none";
        withTempAscPopu.display = "table";
      }
    } else if (filterSortingInput == "dsc") {
      withTempAsc.forEach((item) => {
        item.style.display = "none";
      });
      withTempDsc.forEach((item) => {
        item.style.display = "table";
      });

      if (filterOptionInput == "temp") {
        withTempDscTemp.display = "table";
        withTempDscPopu.display = "none";
        console.log("temp");
      } else if (filterOptionInput == "popu") {
        withTempDscTemp.display = "none";
        withTempDscPopu.display = "none";
      }
    }
  }
}

function validate() {
  var countryValid = regionInput.validity;
  var timePeriodValid = periodInput.validity;

  if (countryValid.valueMissing || timePeriodValid.valueMissing) {
    const alert = document.querySelector(".alert");
    alert.style.display = "flex";
    setTimeout(() => {
      const alert = document.querySelector(".alert");
      alert.style.display = "none";

      // 👇️ hides element (still takes up space on page)
      // box.style.visibility = 'hidden';
    }, 5000);
  }

  if (periodInput.value < 1) {
    const alert = document.querySelector(".alert");
    const alertContent = document.querySelector(".alert-content");
    alertContent.innerHTML = "Please enter a suitable time period";
    alert.style.display = "flex";
    setTimeout(() => {
      const alert = document.querySelector(".alert");
      alert.style.display = "none";

      // 👇️ hides element (still takes up space on page)
      // box.style.visibility = 'hidden';
    }, 5000);
  }

  input.reportValidity();
}

if (similarityCheck.checked == true) {
  similarContainer.style.display = "flex";
} else {
  similarContainer.style.display = "none";
}

similarityCheck.addEventListener("click", function () {
  if (similarityCheck.checked == true) {
    similarContainer.style.display = "flex";
    const alert = document.querySelector(".alert");
    const alertContent = document.querySelector(".alert-content");
    alertContent.innerHTML =
      "This feature will take a while to execute (3-5 mins)";
    alert.style.display = "flex";
    setTimeout(() => {
      const alert = document.querySelector(".alert");
      alert.style.display = "none";

      // 👇️ hides element (still takes up space on page)
      // box.style.visibility = 'hidden';
    }, 10000);
  } else {
    similarContainer.style.display = "none";
  }
});

const select6 = document.getElementsByClassName("select-selected")[5];
const select7 = document.getElementsByClassName("select-selected")[6];

select6.setAttribute("onclick", "similarity()");
select7.setAttribute("onclick", "similarity()");
select8.setAttribute("onclick", "similarity()");

function similarity() {
  const absRelOption = document.getElementById("abs-rel-option").value;
  const regionOption = document.getElementById("region-option").value;
  const mSimLsimOption = document.getElementById("m-sim-l-sim-option").value;
  const simPopuTempOption = document.getElementById(
    "sim-popu-temp-option"
  ).value;

  if (regionOption == "city" || regionOption == "state") {
    const abs = document.querySelectorAll(".city-state.abs");
    const rel = document.querySelectorAll(".city-state.rel");

    if (absRelOption == "abs") {
      abs.forEach((item) => {
        item.style.display = "table";
      });

      rel.forEach((item) => {
        item.style.display = "none";
      });

      if (mSimLsimOption == "mSim") {
        const absSim = document.querySelector(".city-state.abs.sim");
        const absLsim = document.querySelector(".city-state.abs.least-sim");
        const relSim = document.querySelector(".city-state.rel.sim");
        const relLsim = document.querySelector(".city-state.rel.least-sim");

        absSim.style.display = "table";
        absLsim.style.display = "none";
        relSim.style.display = "none";
        relLsim.style.display = "none";
      } else {
        const absSim = document.querySelector(".city-state.abs.sim");
        const absLsim = document.querySelector(".city-state.abs.least-sim");
        const relSim = document.querySelector(".city-state.rel.sim");
        const relLsim = document.querySelector(".city-state.rel.least-sim");

        absSim.style.display = "none";
        absLsim.style.display = "table";
        relSim.style.display = "none";
        relLsim.style.display = "none";
      }
    } else if (absRelOption == "rel") {
      abs.forEach((item) => {
        item.style.display = "none";
      });

      rel.forEach((item) => {
        item.style.display = "table";
      });

      if (mSimLsimOption == "mSim") {
        const absSim = document.querySelector(".city-state.abs.sim");
        const absLsim = document.querySelector(".city-state.abs.least-sim");
        const relSim = document.querySelector(".city-state.rel.sim");
        const relLsim = document.querySelector(".city-state.rel.least-sim");

        absSim.style.display = "none";
        absLsim.style.display = "none";
        relSim.style.display = "table";
        relLsim.style.display = "none";
      } else {
        const absSim = document.querySelector(".city-state.abs.sim");
        const absLsim = document.querySelector(".city-state.abs.least-sim");
        const relSim = document.querySelector(".city-state.rel.sim");
        const relLsim = document.querySelector(".city-state.rel.least-sim");

        absSim.style.display = "none";
        absLsim.style.display = "none";
        relSim.style.display = "none";
        relLsim.style.display = "table";
      }
    }
  } else {
    const abs = document.querySelectorAll(".country.abs");
    const rel = document.querySelectorAll(".country.rel");
    if (absRelOption == "abs") {
      abs.forEach((item) => {
        item.style.display = "table";
      });

      rel.forEach((item) => {
        item.style.display = "none";
      });

      if (mSimLsimOption == "mSim") {
        const absSim = document.querySelectorAll(".country.abs.sim");
        const absLsim = document.querySelectorAll(".country.abs.least-sim");
        const relSim = document.querySelectorAll(".country.rel.sim");
        const relLsim = document.querySelectorAll(".country.rel.least-sim");

        absSim.forEach((item) => {
          item.style.display = "table";
        });
        absLsim.forEach((item) => {
          item.style.display = "none";
        });
        relSim.forEach((item) => {
          item.style.display = "none";
        });
        relLsim.forEach((item) => {
          item.style.display = "none";
        });

        if (simPopuTempOption == "popu") {
          const abdSimPopu = document.querySelector(".country.abs.sim.popu");
          const absSimTemp = document.querySelector(".country.abs.sim.temp");
          const absSimBoth = document.querySelector(
            ".country.abs.sim.temp-popu"
          );

          abdSimPopu.style.display = "table";
          absSimTemp.style.display = "none";
          absSimBoth.style.display = "none";
        } else if (simPopuTempOption == "temp") {
          const abdSimPopu = document.querySelector(".country.abs.sim.popu");
          const absSimTemp = document.querySelector(".country.abs.sim.temp");
          const absSimBoth = document.querySelector(
            ".country.abs.sim.temp-popu"
          );

          abdSimPopu.style.display = "none";
          absSimTemp.style.display = "table";
          absSimBoth.style.display = "none";
        } else if (simPopuTempOption == "both") {
          const abdSimPopu = document.querySelector(".country.abs.sim.popu");
          const absSimTemp = document.querySelector(".country.abs.sim.temp");
          const absSimBoth = document.querySelector(
            ".country.abs.sim.temp-popu"
          );

          abdSimPopu.style.display = "none";
          absSimTemp.style.display = "none";
          absSimBoth.style.display = "table";
        }
      } else {
        const absSim = document.querySelectorAll(".country.abs.sim");
        const absLsim = document.querySelectorAll(".country.abs.least-sim");
        const relSim = document.querySelectorAll(".country.rel.sim");
        const relLsim = document.querySelectorAll(".country.rel.least-sim");

        absSim.forEach((item) => {
          item.style.display = "none";
        });
        absLsim.forEach((item) => {
          item.style.display = "table";
        });
        relSim.forEach((item) => {
          item.style.display = "none";
        });
        relLsim.forEach((item) => {
          item.style.display = "none";
        });

        if (simPopuTempOption == "popu") {
          const abdSimPopu = document.querySelector(
            ".country.abs.least-sim.popu"
          );
          const absSimTemp = document.querySelector(
            ".country.abs.least-sim.temp"
          );
          const absSimBoth = document.querySelector(
            ".country.abs.least-sim.temp-popu"
          );

          abdSimPopu.style.display = "table";
          absSimTemp.style.display = "none";
          absSimBoth.style.display = "none";
        } else if (simPopuTempOption == "temp") {
          const abdSimPopu = document.querySelector(
            ".country.abs.least-sim.popu"
          );
          const absSimTemp = document.querySelector(
            ".country.abs.least-sim.temp"
          );
          const absSimBoth = document.querySelector(
            ".country.abs.least-sim.temp-popu"
          );

          abdSimPopu.style.display = "none";
          absSimTemp.style.display = "table";
          absSimBoth.style.display = "none";
        } else if (simPopuTempOption == "both") {
          const abdSimPopu = document.querySelector(
            ".country.abs.least-sim.popu"
          );
          const absSimTemp = document.querySelector(
            ".country.abs.least-sim.temp"
          );
          const absSimBoth = document.querySelector(
            ".country.abs.least-sim.temp-popu"
          );

          abdSimPopu.style.display = "none";
          absSimTemp.style.display = "none";
          absSimBoth.style.display = "table";
        }
      }
    } else if (absRelOption == "rel") {
      abs.forEach((item) => {
        item.style.display = "none";
      });

      rel.forEach((item) => {
        item.style.display = "table";
      });

      if (mSimLsimOption == "mSim") {
        const absSim = document.querySelectorAll(".country.abs.sim");
        const absLsim = document.querySelectorAll(".country.abs.least-sim");
        const relSim = document.querySelectorAll(".country.rel.sim");
        const relLsim = document.querySelectorAll(".country.rel.least-sim");

        absSim.forEach((item) => {
          item.style.display = "none";
        });
        absLsim.forEach((item) => {
          item.style.display = "none";
        });
        relSim.forEach((item) => {
          item.style.display = "table";
        });
        relLsim.forEach((item) => {
          item.style.display = "none";
        });

        if (simPopuTempOption == "popu") {
          const abdSimPopu = document.querySelector(".country.rel.sim.popu");
          const absSimTemp = document.querySelector(".country.rel.sim.temp");
          const absSimBoth = document.querySelector(
            ".country.rel.sim.temp-popu"
          );

          abdSimPopu.style.display = "table";
          absSimTemp.style.display = "none";
          absSimBoth.style.display = "none";
        } else if (simPopuTempOption == "temp") {
          const abdSimPopu = document.querySelector(".country.rel.sim.popu");
          const absSimTemp = document.querySelector(".country.rel.sim.temp");
          const absSimBoth = document.querySelector(
            ".country.rel.sim.temp-popu"
          );

          abdSimPopu.style.display = "none";
          absSimTemp.style.display = "table";
          absSimBoth.style.display = "none";
        } else if (simPopuTempOption == "both") {
          const abdSimPopu = document.querySelector(".country.rel.sim.popu");
          const absSimTemp = document.querySelector(".country.rel.sim.temp");
          const absSimBoth = document.querySelector(
            ".country.rel.sim.temp-popu"
          );

          abdSimPopu.style.display = "none";
          absSimTemp.style.display = "none";
          absSimBoth.style.display = "table";
        }
      } else if (mSimLsimOption == "lSim") {
        const absSim = document.querySelectorAll(".country.abs.sim");
        const absLsim = document.querySelectorAll(".country.abs.least-sim");
        const relSim = document.querySelectorAll(".country.rel.sim");
        const relLsim = document.querySelectorAll(".country.rel.least-sim");

        absSim.forEach((item) => {
          item.style.display = "none";
        });
        absLsim.forEach((item) => {
          item.style.display = "none";
        });
        relSim.forEach((item) => {
          item.style.display = "none";
        });
        relLsim.forEach((item) => {
          item.style.display = "table";
        });

        if (simPopuTempOption == "popu") {
          const abdSimPopu = document.querySelector(
            ".country.rel.least-sim.popu"
          );
          const absSimTemp = document.querySelector(
            ".country.rel.least-sim.temp"
          );
          const absSimBoth = document.querySelector(
            ".country.rel.least-sim.temp-popu"
          );

          abdSimPopu.style.display = "table";
          absSimTemp.style.display = "none";
          absSimBoth.style.display = "none";
        } else if (simPopuTempOption == "temp") {
          const abdSimPopu = document.querySelector(
            ".country.rel.least-sim.popu"
          );
          const absSimTemp = document.querySelector(
            ".country.rel.least-sim.temp"
          );
          const absSimBoth = document.querySelector(
            ".country.rel.least-sim.temp-popu"
          );

          abdSimPopu.style.display = "none";
          absSimTemp.style.display = "table";
          absSimBoth.style.display = "none";
        } else if (simPopuTempOption == "both") {
          const abdSimPopu = document.querySelector(
            ".country.rel.least-sim.popu"
          );
          const absSimTemp = document.querySelector(
            ".country.rel.least-sim.temp"
          );
          const absSimBoth = document.querySelector(
            ".country.rel.least-sim.temp-popu"
          );

          abdSimPopu.style.display = "none";
          absSimTemp.style.display = "none";
          absSimBoth.style.display = "table";
        }
      }
    }
  }
}
