import { dump,  innerJoin, leftOuterJoin} from "./database.js";
import { seed } from "./seed.js";

export const personTable = []
export const addressTable = []

if (process.argv.length === 2) {
    console.error('Please provide JOIN type (INNER_JOIN, OUTER_LEFT_JOIN');
    process.exit(1);
  }
 

seed(personTable, addressTable);

const joinType = process.argv[2]
switch(joinType){
    case 'INNER_JOIN':
        dump(innerJoin(personTable, addressTable, (person, address) => person.addressId === address.id));
        break;

    case 'OUTER_LEFT_JOIN':
        dump(leftOuterJoin(personTable, addressTable, (person, address) => person.addressId === address.id));
        break;
    default:
        console.log("No such join type")

}


