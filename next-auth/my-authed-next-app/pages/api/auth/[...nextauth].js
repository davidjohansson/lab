import NextAuth from "next-auth"

import KeycloakProvider from "next-auth/providers/keycloak";
import PostgresAdapter from "@auth/pg-adapter"
import { Pool } from "pg"

const pool = new Pool({
  connectionString: process.env.DATABASE_URL
})

export const authOptions = {
  adapter: PostgresAdapter(pool),
  providers: [
    KeycloakProvider({
      clientId: process.env.KEYCLOAK_ID,
      clientSecret: process.env.KEYCLOAK_SECRET,
      issuer: process.env.KEYCLOAK_ISSUER,
    })
  ]
}

export default NextAuth(authOptions)
