# react-native-razorpay

[![npm](https://img.shields.io/npm/l/express.svg)]()
[![NPM Version](http://img.shields.io/npm/v/react-native-razorpay.svg?style=flat)](https://www.npmjs.com/package/react-native-dhanyatra)
[![NPM Downloads](https://img.shields.io/npm/dm/react-native-razorpay.svg?style=flat)](https://npmcharts.com/compare/react-native-dhanyatra?minimal=true)
[![install size](https://packagephobia.com/badge?p=react-native-razorpay@2.1.35)](https://packagephobia.com/result?p=react-native-dhanyatra@1.0)

[![NPM](https://nodei.co/npm/react-native-dhanyatra.png?downloads=true)](https://nodei.co/npm/react-native-razorpay/)

React Native wrapper around our Android and iOS mobile SDKs

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Requirements](#requirements)
- [Linking](#linking)
- [Usage](#usage)
- [Example App](https://github.com/dhanyatra/react-native-dhanyatra/tree/master/example)
- [Proguard Rules](#proguard-rules)
- [Notes](#things-to-be-taken-care)
- [FAQ's](#faqs)
- [Contributing](#contributing)
- [License](#license)

The following documentation is only focussed on the react-native wrapper around our Android and iOS sdks. To know more about our react-native SDK, refer to the following documentation -

https://dhanyatra.brighthustle.in/docs/payment-gateway/react-native-integration/

To know more about Dhanyatra payment flow and steps involved, read up here:
<https://docs.dhanyatra.brighthustle.in/docs>

## Prerequisites

- Learn about the <a href="https://dhanyatra.brighthustle.in/docs/payment-gateway/payment-flow/" target="_blank">Dhanyatra Payment Flow</a>.
- Sign up for a <a href="https://dashboard.dhanyatra.brighthustle.in/#/access/signin">dhanyatra Account</a> and generate the <a href="https://dhanyatra.brighthustle.in/docs/payment-gateway/dashboard-guide/settings/#api-keys/" target="_blank">API Keys</a> from the Dhanyatra Dashboard. Using the Test keys helps simulate a sandbox environment. No actual monetary transaction happens when using the Test keys. Use Live keys once you have thoroughly tested the application and are ready to go live.

## Installation

Using npm:

```shell
npm install --save react-native-dhanyatra
```

or using yarn:

```shell
yarn add react-native-dhanyatra
```

For Expo Users:

```shell
npx expo install react-native-dhanyatra
```

## Requirements

- iOS 10.0+ / macOS 10.12+ / tvOS 10.0+ / watchOS 3.0+
- Xcode 11+
- Swift 5.1+

## Linking

### Automatic

<details>
    <summary>iOS</summary>

### For React Native 0.60+

```sh
# install
npm install react-native-dhanyatra --save
cd ios && open podfile # Change the platform from iOS 9.0 to 10.0
pod install && cd .. # CocoaPods on iOS needs this extra step
# run
yarn react-native run-ios
```

### For React Native 0.59 and lower

1. `$ npm install react-native-dhanyatra --save` // Install the Dhanyatra React Native Standard SDK using the npm command.

2. `react-native link react-native-dhanyatra` // Link the SDK with React Native Project using Xcode.

3. Drag the `Dhanyatra.framework` file from the Libraries folder and drop it under the root folder, for more info follow [this link](https://dhanyatra.brighthustel.in/docs/payment-gateway/react-native-integration/standard/#step-2---link-the-sdk-with-react),
   after this go to **Target** > **General Settings**> **Framework, Libraries and Embedded Content** section, set the **Embed** status of Dhanyatra.framework to **Embed & Sign**.

4. Also make sure the dhanyatra framework is added in the embedded binaries section and you have Always Embed Swift
Standard Binaries set to yes in build settings.
</details>

### Manual

<details>
    <summary>iOS (via CocoaPods)</summary>

Add the following line to your build targets in your `Podfile`

`pod 'react-native-dhanyatra', :path => '../node_modules/react-native-dhanyatra'`

Then run `pod install`

</details>

<details>
    <summary>iOS (without CocoaPods)</summary>

In XCode, in the project navigator:

- Right click _Libraries_
- Add Files to _[your project's name]_
- Go to `node_modules/react-native-dhanyatra`
- Add the `.xcodeproj` file

In XCode, in the project navigator, select your project.

- Add the `libRNDeviceInfo.a` from the _deviceinfo_ project to your project's _Build Phases ➜ Link Binary With Libraries_
- Click `.xcodeproj` file you added before in the project navigator and go the _Build Settings_ tab. Make sure _All_ is toggled on (instead of _Basic_).
- Look for _Header Search Paths_ and make sure it contains both `$(SRCROOT)/../react-native/React` and `$(SRCROOT)/../../React`
- Mark both as recursive (should be OK by default).

Run your project (Cmd+R)

</details>

<details>
    <summary>Android </summary>

1. Open up `android/app/src/main/java/[...]/MainApplication.java`

- Add `import com.dhanyatra.rn.DhanyatraPackage;` to the imports at the top of
  the file
- Add `new DhanyatraPackage()` to the list returned by the `getPackages()` method

2. Append the following lines to `android/settings.gradle`:
   ```gradle
   include ':react-native-dhanyatra'
   project(':react-native-dhanyatra').projectDir = new File(rootProject.projectDir,   '../node_modules/react-native-dhanyatra/android')
   ```
3. Insert the following lines inside the dependencies block in
`android/app/build.gradle`:
`gradle
    implementation project(':react-native-dhanyatra')
    `
</details>

### Note for Expo Applications:

After adding the react-native-dhanyatra package,the option to `prebuild` the app must be used(this generates the android/ios platform folders in the project to use native-modules). Command for which,

```shell
npx expo prebuild
```

After which the application will be installed on the device/emulator.

```shell
npx expo run:[ios|android] --device
```

## Usage

Sample code to integrate with Dhanyatra can be found in
[App.tsx][App.tsx] in the included example directory.

To run the example, simply do the following in example directory and then
link iOS SDK as explained in the previous section:

`$ npm i`

### Steps

1. Import DhanyatraCheckout module to your component:

   ```js
   import DhanyatraCheckout from 'react-native-dhanyatra';
   ```

2. Call `DhanyatraCheckout.startPayment` method with the payment `options`. The method
   returns a **JS Promise** where `then` part corresponds to a successful payment
   and the `catch` part corresponds to payment failure.
   ```js
   <TouchableHighlight onPress={() => {
   var options = {
    description: 'Credits towards consultation',
    image: 'https://i.imgur.com/3g7nmJC.png',
    currency: 'INR',
    key: '', // Your api key
    amount: '5000',
    name: 'foo',
    prefill: {
      email: 'void@brighthustle.in',
      contact: '9191919191',
      name: 'Dhanyatra'
    },
    theme: {
        color: {
            text: '#ffa800',
            base: '#ffa800'
        }
    },
   }
   DhanyatraCheckout.open(options).then((data) => {
    // handle success
    alert(`Success: ${data.payment_id}`);
   }).catch((error) => {
    // handle failure
    alert(`Error: ${error.code} | ${error.description}`);
   });
   }}>
   ```

A descriptive [list of valid options for checkout][options] is available (under
Manual Checkout column).

## Proguard Rules

If you are using proguard for your builds, you need to add following lines to proguard files

```

-keepattributes _Annotation_
-dontwarn com.dhanyatra.**
-keep class com.dhanyatra.** {_;}
-optimizations !method/inlining/
-keepclasseswithmembers class _ {
public void onPayment\*(...);
}

```

## Things to be taken care

- The react native plugin is wrapper around native SDK, so it doesn't work with the tools like expo which doesn't support native modules.

## FAQ's

- For UPI Intent in iOS, the info.plist in iOS should be modified to include `LSApplicationQueriesSchemes`
  - For Bare React-Native Apps:
    - info.plist can directly be modified from the xcode project. LSApplicationQueriesSchemes takes as array value and can currently include only ["tez","phonepe","paytmmp"]
  - For Expo Apps:
    - Directly modifying info.plist is discouraged, and hence this should be added in app.json
    ```shell
      "ios": {
        "infoPlist": {
          "LSApplicationQueriesSchemes": [
             "tez",
             "phonepe",
             "paytmmp"
          ]
        }
      }
    ```
    - P.S: The apps won't be visible if the application is run with metro builder. The info.plist is generated successfully and integrated only when the app is built as standalone app.
- Still having trouble with integrating our payment gateway? Follow [this link](https://github.com/dhanyatra/react-native-dhanyatra/wiki/FAQ's) for more info.

## Contributing

See the [CONTRIBUTING] document. Thank you, [contributors]!

## License

react-native-dhanyatra is Copyright (c) 2020 Bright Hustle Pvt. Ltd.
It is distributed under [the MIT License][LICENSE].

We ♥ open source software!
See [our other supported plugins / SDKs][integrations]
or [contact us][contact] to help you with integrations.

[contact]: mailto:integrations@dhanyatra.brighthustle.in?subject=Help%20with%20React%20Native 'Send us a mail'
[CONTRIBUTING]: CONTRIBUTING.md 'Our contributing guidelines'
[contributors]: https://github.com/dhanyatra/react-native-dhanyatra/graphs/contributors 'List of contributors'
[index.js]: example/src/App.tsx 'App.tsx'
[integrations]: https://dhanyatra.brighthustle.in/integrations 'List of our integrations'
[ios-docs]: https://docs.dhanyatra.brighthustle.in/v1/page/ios-integration 'Documentation for the iOS Integration'
[LICENSE]: /LICENSE 'MIT License'
[options]: https://docs.dhanyatra.brighthustle.in/docs/checkout-form#checkout-fields 'Checkout Options'
[wiki]: https://github.com/dhanyatra/react-native-dhanyatra/wiki/Manual-Installation
