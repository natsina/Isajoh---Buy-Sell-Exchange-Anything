

# E-commerce appplication
Isajoh is an online marketplace that allows anyone to easily and convieniently buy, sell, and exchange anything they want hassle-free.


# Getting Started

# Software Installation

To run the application, you have to installed the JDK 
(http://www.oracle.com/technetwork/java/javase/downloads/index.html), make sure to install all 
components. 
You also need to download Android Studio (http://developer.android.com/sdk/index.html) which will also 
install the SDK manager. 

# SDK Manager 

The installer, should automatically download all required components. If you do a manual setup, download 
at least the following components using the SDK manager: 

# In the tools folder: 
 SDK Tools 
 SDK Platform Tools 
 SDK Build Tools 

# In the extras folder; 

 Google Play Services 
 Android Support Library 
 Android Support Repository 
 Google Repository 
 Google USB Driver 

And at least one SDK version (At least Android 5.0).

# SETUP

# Clone the Repo.

git clone https://github.com/natsina/Isajoh---Buy-Sell-Exchange-Anything.git

# Importing Application

To import the template, simply open Android Studio and import the template. Make sure to wait 
for all the processes (the Gradle Build) to complete before continuing to the next step. 

# Addition Steps

Sometimes, the Gradle Build fails, here are some additional steps you can take: 
Android Studio might ask you to do download some additional SDK components in the 'messages' 
tab, simply follow the instructions to install and download the additional components to fix this. 

Also, please make sure the app is targeting at least Marshmallow and that all the libraries are linked:

 Libraries (.jar) from the libs folder 
 All the Gradle defined libraries are linked (as found in app/build.gradle) 
 Make sure to wait until all loading is complete before manually (re)adding any modules 
and/or libraries!

You can now run the application using the Android Virtual Device (AVD) that the emulator can use to install and run your application or you can use a real Android device by connecting it via USB.

# Authors

Noris Atsina




