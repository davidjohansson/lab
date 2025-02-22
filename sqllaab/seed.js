import * as data from "./seed.json" assert { type: 'json' };
import { insertPerson, insertAddress } from "./database.js";

export function seed(personTable, addressTable) {
  data.default.persons.forEach((person) => {
    insertPerson(person, personTable);
  });

  data.default.addresses.forEach((address) => {
    insertAddress(address, addressTable);
  });
}
