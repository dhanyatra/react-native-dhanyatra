// @ts-nocheck
interface Config {
  display: {
    blocks: Array<Block>;
    sequence: Array<string>;
    preferences: Preferences;
  };
}

interface Theme {
  color: Color;
}

interface Color {
  text: string;
  base: string;
}

interface Block {
  preferred: Preferred;
}

interface Preferred {
  name: string;
  instruments: Array<Instrument>;
}

interface Instrument {
  method: string;
  flows: Array<string>;
  apps: Array<string>;
}

interface Preferences {
  show_default_blocks: boolean;
}

interface Ark {
  user_id: string;
  org_id: string;
  mode: string;
  amount?: string;
  pay_complete?: boolean;
}

// eslint-disable-next-line @typescript-eslint/no-redeclare -- backward compatibility; it can be used as a type and as a value
export interface PaymentOptions {
  key: string;
  currency: string;
  amount?: string;
  config?: Config;
  ark?: Ark;
  theme?: Theme;
}
