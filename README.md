# BaseAuth
Phone number authentication leveraging on different phone number authentication platform like firebase, sinch e.t.c

## Sample

## Adding as a dependency

#### Core
*Not Available in 1.0.1*

core provides access to other underlying auth platform. Only use core if you intend to perform phone number authetication using multiple auth providers.
```gradle
dependencies {
    compile 'com.belvi.auth:baseauth-core:1.0.0'
}
```

#### Firebase

This module handles phone number authentication using firebase. 
```gradle
dependencies {
    compile 'com.belvi.auth:baseauth-firebase:1.0.1'
}
```

#### Sinch

This module handles phone number authentication using firebase. 

*Not Available in 1.0.1*

```gradle
dependencies {
    compile 'com.belvi.auth:baseauth-sinch:1.0.0'
}
```
## Using the Library:

#### As Firebase :

```java
        FireAuthActivity.startFirebasePhoneAuth(this, new FirebaseAuthListener() {
            @Override
            public void authIgnored() {
                // user existed without autheticating
            }

            @Override
            public void helpClicked(Context context) {
                // Help icon is cliced. You might want to do something here.

            }

            @Override
            public void onAuthCompleted(PhoneAuthCredential credential, String phoneNumber) {
                // do something with Firebase PhoneAuthCredential and the autheticated phone number
            }
        }, R.style.BaseAuthStyle);
```

#### Styling and Theming 

define style in styles.xml

```xml
    <style name="BaseAuthStyle">
        <item name="ba_show_help">true</item>
    </style>
```

`ba_show_help` determines if the help icon will be shown or not.

layout resources are referenced in corresponding resource xml files.<br/>
you can override any of the resource to suit your need.

##### color.xml

```xml
<resources>
    <color name="ba_colorPrimary">#42a5f5</color>
    <color name="ba_colorPrimaryDark">#253493</color>
    <color name="ba_colorAccent">#FF4081</color>
    <color name="ba_txt_error">#FF4081</color>
    <color name="ba_blue">#42a5f5</color>
    <color name="ba_background">#fcfcfc</color>
    <color name="ba_btn_disabled">#dfdfdf</color>
    <color name="ba_btn_enabled">#ffffff</color>
    <color name="ba_txt_grey">#696969</color>
    <color name="ba_auth_code">#42a5f5</color>
    <color name="ba_white">#FFFFFF</color>
    <color name="ba_country_selector_code_color">#787878</color>
    <color name="ba_country_selector_bg_color">#FFFFFF</color>
    <color name="ba_country_selector_name_color">#000000</color>
</resources>

```

##### strings.xml

```
<resources>
    <string name="app_name">BaseAuth</string>
    <string name="help">Help</string>
    <string name="select_country">Select your country</string>
    <string name="type_in">Please type in the verification code sent to %s</string>
    <string name="verify_number">Verify your number</string>
    <string name="phone_hint">Your phone number</string>
    <string name="cc_hint">Your country code</string>
    <string name="btn_next_title">Next</string>
    <string name="verification_code">Verification Code</string>
    <string name="received_question">Didn\'t receive  code ?</string>
    <string name="wait_field">Please wait</string>
    <string name="help_link">http://www.example.com</string>
    <string name="charges_desc">We will send you a one-off SMS message. Carrier charges may apply.</string>
    <string name="details_desc">Enter your country code and phone number for verification</string>
</resources>
```

##### dimens.xml

```xml
<resources>
    <dimen name="ba_layout_padding">32dp</dimen>
    <dimen name="verification_code_text_size">24sp</dimen>
    <dimen name="vc_txt_size">18sp</dimen>
    <dimen name="type_in_desc_txt_size">16sp</dimen>
    <dimen name="type_in_desc_margin_top">32dp</dimen>
    <dimen name="type_in_desc_margin_bottom">16dp</dimen>
    <dimen name="verification_layout_padding">32dp</dimen>
    <dimen name="verification_code_width">42dp</dimen>
    <dimen name="details_desc">16sp</dimen>
    <dimen name="details_margin_top">16dp</dimen>
    <dimen name="phone_field_text_size">18sp</dimen>
    <dimen name="next_btn_txt_size">14sp</dimen>
    <dimen name="next_btn_padding">16dp</dimen>
    <dimen name="wait_field_txt_size">16sp</dimen>
    <dimen name="receive_code_confirmation">14sp</dimen>
    <dimen name="receive_code_confirmation_top_margin">16dp</dimen>
    <dimen name="verification_status_text_size">12sp</dimen>
    <dimen name="verification_status_top_margin">16dp</dimen>
</resources>
```
