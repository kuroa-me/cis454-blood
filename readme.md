# Blood Bank -- we take your blood

## Overview
For this app, the normal users(donors and requesters) will use mobile phone to use this app(using android studio to write it), administrater will use website to manage user information(which is easier for administrater). Therefore, our project has mobile app for normal users and website for administraters.


## Usage
### Website
To visit website, you can go to frontend-admin directory and open the login.html file.
To login with administrater account, enter username "admin" with password "1", then you can login successfully and manage user information.


### Mobile
The mobile client is for Android platform. To install it, kindly change directory to `cis454-blood/frontend-android/app/release/app-release.apk`. Put it into your android device and then double click it to proceed.

### Backend
We will provide backend server until 7/1/2019. After that time, you could use the files in `cis454-blood/backend` to host your own backend server. All api calls are located in `cis454-blood/doc/api.md`. Do remember to change the server address in the frontend programs.


## Functions
### Donor
1. Upload Information
2. Respond to donate request
3. Donate blood
4. View donation history

### Requestor
1. Upload Information
2. Make blood request
3. Check request status
4. View request history

### Administrator
1. Check user information
2. Remove user
3. Modify user
4. Check donation history
5. Remove donation history
6. Check all blood types
7. Add blood type

### Mobile Client
1. Register new user
2. Provide login for donor and requestor

### Website Client
1. Provide login for administrator