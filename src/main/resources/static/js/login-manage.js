//load in the api
let weather = {
  apiKey: "96882b946adfd636d847d55646da73b6",
  fetchWeather: function (city) {
    fetch(
      "https://api.openweathermap.org/data/2.5/weather?q=" +
        city +
        "&units=metric&appid=" +
        this.apiKey
    )
      .then((response) => response.json())
      .then((data) => this.displayWeather(data));
  },

  //take data from the api
  displayWeather: function (data) {
    const { name } = data;
    const { description, icon } = data.weather[0];
    const { temp, temp_min, temp_max, humidity, pressure } = data.main;
    const { speed } = data.wind;
    const { timezone } = data;

    for (let i = 0; i < description.length; i++) {
      if (description[i] == " ") {
        var newDesc =
          description[0].toUpperCase() +
          description.slice(1, i) +
          " " +
          description[i + 1].toUpperCase() +
          description.slice(i + 2);
      }

      if (!description[i] == " ") {
        var newSesc2 =
          description[0].toUpperCase() + description.slice(1, i + 1);
      }
    }

    if (newDesc == undefined) {
      var newDesc = newSesc2;
    }

    console.log(description);
    console.log(newDesc);
    //display data in to page
    document.querySelector(".icon").src =
      "https://openweathermap.org/img/wn/" + icon + "@2x.png";
    document.querySelector(".temp").innerText = "Temperature: " + temp + "°C";
    document.querySelector(".city").innerText = name;
    document.querySelector(".description").innerText = newDesc;
    document.querySelector(".humidity").innerText = humidity + "%";
    document.querySelector(".wind").innerText = speed + "m/s";
    document.querySelector(".tempMinMax").innerText =
      temp_min + "°C / " + temp_max + "°C";

    document.querySelector(".time-zone").innerText = timezone / 3600 + "h";
    document.querySelector(".pressure").innerText = pressure + "hPa";
  },
  search: function () {
    this.fetchWeather(document.querySelector(".search-bar").value);
  },
};
//search button
document
  .querySelector("#country-search")
  .addEventListener("click", function () {
    weather.search();
  });

//enter function
document
  .querySelector(".search-bar")
  .addEventListener("keyup", function (event) {
    if (event.key == "Enter") {
      weather.search();
    }
  });

//default Api search
weather.fetchWeather("Hanoi");
