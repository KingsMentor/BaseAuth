# BaseAuth
Phone number authentication leveraging on different phone number authentication platform like firebase, sinch e.t.c

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
