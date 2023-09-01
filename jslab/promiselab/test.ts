
interface A {
  a: string
}

interface B {
  b: string
}

type AorBorBoth = A | B
const c : AorBorBoth = {a: "a", b: "b"}


console.log((c as A).a)
//////////////////////
const a1 = () => {

  interface A {
    a: string
  }

  interface B {
    b: string
  }

  type AandB = A & B
  const c : AandB = {a: "a", b: "b"}

  console.log(c.a)
  console.log(c.b)
}


//////////////////////

const a = () => {

  type A = {
    a: string
  }

  type B = {
    b: string
  }

  //går inte
  // type C extends B = {
  //   c: string
  // }

}

//////////////////////

const b= () => {

  type A = {
    a: string
    b: string
  }

  const f = (a: A) => {
    console.log(a.b);
  }

  const anA : A = {a: "a", b:"b"}
  f(anA);

  // A är bara ett alias för typen
  // {
  //   a: string
  //   b: string
  // }
  // Så om ett objekt har den formen har det implicit typen

  f({a: "a", b:"b"})

}
