# COMP.SE.110

Software Design group project

## Prerequisites

- Java 23
- Docker (v?)

## Getting started

To start the project backend on Docker, run the following command in the root directory of the project:

```bash
docker compose up
```

## Links

- [Frontend repository](https://github.com/BarryAlanPan/comp.se.110-frontend)
- [Figma design prototype](https://www.figma.com/design/XxQbtN593ZhB8Ut8DBjTWL/Structure-draft?node-id=0-1&t=GP6AdvPkAk87nYPu-1)

## Endpoints

Endpoints to retrieve and update the current user's information.

```
GET /api/profile/{id}
PATCH /api/profile/{id}
```
To retrieve all user profiles for admin tasks use
```
GET /api/profile
```

Endpoints for current user's meal plan. (Not implemented yet)

```
GET /mealplan - returns a single meal plan
PATCH /mealplan - returns the updated meal plan
```

Endpoints for recipes. Should proxy the Spoonacular API at least partly.

```
GET /recipes/{id} - returns a single recipe based on id
API to proxy: https://api.spoonacular.com/recipes/{id}/information

GET /recipes/search - returns a list of recipes based on query parameters
API to proxy: https://api.spoonacular.com/recipes/complexSearch
Note that we don't need all the parameters.
```

We also need endpoints for some select options for different search parameters.
