/*
person schema:
id: number
firstName: string
lastName: string
addressId: number

address schema:
id: number
street: string
city: string
*/

export function insertPerson(person, personTable) {
  personTable.push(person);
}

export function insertAddress(address, addressTable) {
  addressTable.push(address);
}

export function dump(table) {
  table.forEach((row) => console.log(row));
  console.log("\n");
}

  const formatResultRow = (person, address) => ({
    id: person.id,
    name: person.firstName,
    street: address?.street,
  });
  
export function innerJoin(personTable, addressTable, matchFunction) {
  const matchAll = () => true;
  const match = matchFunction || matchAll;

  return cartesianProduct(personTable, addressTable, match, formatResultRow);
}

export function leftOuterJoin(personTable, addressTable, matchFunction) {
  //All from person table regardless of match, only matching from address function
  const result = [];

  personTable.forEach((p) => {
    const countBeforeMatching = result.length;
    addressTable.forEach((a) => {
      if (matchFunction(p, a)) {
        result.push(formatResultRow(p, a));
      } 
    });
    const countAfterMatching = result.length;
    if(countBeforeMatching === countAfterMatching) {
        result.push(formatResultRow(p, null))
    }
  });

  return result;
}

function cartesianProduct(list1, list2, match, formatResultRow) {
  const result = [];
  list1.forEach((list1Row) => {
    list2.forEach((list2Row) => {
      if (match(list1Row, list2Row)) {
        result.push(formatResultRow(list1Row, list2Row));
      }
    });
  });
  return result;
}
