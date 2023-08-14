export interface InfoResponse {
  'display-ribbon-on-profiles'?: string;
  git?: any;
  build?: any;
  ['active-profiles']?: string[];
}

export class ProfileInfo {
  constructor(
    public activeProfiles?: string[],
    public ribbonEnv?: string,
    public inProduction?: boolean,
    public openAPIEnabled?: boolean
  ) {}
}
