const myPromise = new Promise((resolve, reject) => {
  setTimeout(() => {
    resolve("foo");
  }, 300);
});

myPromise
.then(str => {
 console.log("This will print: " + str)
  return str;
})
.then(str => {
  console.log("This will also print: " + str)
})
.then(str => {
  console.log("This will print undefined: " + str)
})
