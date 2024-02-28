const myResolvingPromise = new Promise((resolve, reject) => {
  setTimeout(() => {
    resolve("resolving");
  }, 300);
});


const myRejectingPromise = new Promise((resolve, reject) => {
  setTimeout(() => {
    reject("rejecting");
  }, 300);
});

function resolveFunction(str) {
  console.log("Resolving " + str)
  return str;
}


function rejectFunction(str) {
  console.log("Rejecting " + str)
  return str;
}

myResolvingPromise
.then(resolveFunction, rejectFunction)
.then(str => {
  console.log("This will also print: " + str)
})
.then(str => {
  console.log("This will print undefined: " + str)
})
