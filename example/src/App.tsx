import type { Node } from '@babel/core';
import {} from 'react';
import {
  View,
  Button,
  SafeAreaView,
  ScrollView,
  StatusBar,
  useColorScheme,
} from 'react-native';
import DhanyatraCheckout, { type PaymentOptions } from 'react-native-dhanyatra';
import { Colors, Header } from 'react-native/Libraries/NewAppScreen';

const App: () => Node = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}
      >
        <Header />
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}
        >
          <Button
            title={'Pay with Dhanyatra'}
            onPress={() => {
              var options: PaymentOptions = {
                key: 'Mg.EXfuJYaDVQxmnPfh7PQqc58yEK0Ib70XX92qU7T51qg1vUzilbHQNNMMWrla', //Key here
                currency: 'INR',
                amount: '400',
                config: {
                  display: {
                    blocks: [
                      {
                        preferred: {
                          name: 'Preferred Payment',
                          instruments: [
                            {
                              method: 'upi',
                              flows: ['qr', 'intent'],
                              apps: ['phonepe', 'google_pay', 'paytm'],
                            },
                          ],
                        },
                      },
                    ],
                    sequence: ['block.preferred'],
                    preferences: {
                      show_default_blocks: true, // Should Checkout show its default blocks?
                    },
                  },
                },
                theme: {
                  color: {
                    text: '#ffa800',
                    base: '#ffa800',
                  },
                },
                ark: {
                  user_id: '4437',
                  org_id: '8',
                  mode: 'ark_app',

                  pay_complete: false,
                },
              };
              DhanyatraCheckout.open(options)
                .then((data) => {
                  // handle success
                  console.log('success ::', data);
                })
                .catch((error) => {
                  // handle failure
                  console.log('error ::', error);
                });
            }}
          />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

export default App;
