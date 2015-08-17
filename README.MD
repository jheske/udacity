## Synopsis

This repository contains my Udacity Android Developer Nanodegree Project 1: Popular Movies, Stage 1

Udacity's description of the project is as follows:

In this project, you will build an app to help users discover popular and recent movies. You will build a clean UI, sync to a server, and present information to the user.

## State of the Project

This project is designed to meet all of the requirements as per Udacity's rubric.  

In addition it contains some features in preparation for

## Installation

You can fork this repo or clone it: git clone https://github.com/jheske/udacity.git
Note that all of my projects are stored in this repo, so you will have to clone the entire repo to get this project.

## themoviedb.org api key

In order to compile and run the app, you will need to acquire a valid api key from www.themoviedb.org.  In tmdb.java replace the line:

                    requestFacade.addQueryParam(PARAM_API_KEY, ApiKey.getApiKey());

with

                    requestFacade.addQueryParam(PARAM_API_KEY, "your api key");


## Contributors

Jill Heske

## License

A short snippet describing the license (MIT, Apache, etc.)