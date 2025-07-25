/** Default theme settings */
export const themeSettings: App.Theme.ThemeSetting = 
{themeScheme:"light",themeColor:"#6366f1",otherColor:{info:"#2080f0",success:"#52c41a",warning:"#faad14",error:"#f5222d"},resetCacheStrategy:"refresh",layout:{mode:"vertical",scrollMode:"content"},page:{animate:true,animateMode:"fade-slide"},header:{height:56,breadcrumb:{visible:false,showIcon:true},multilingual:{visible:false}},fixedHeader:true,sider:{width:220,collapsedWidth:64,mixWidth:90,mixCollapsedWidth:64,mixChildMenuWidth:200},footer:{visible:true,fixed:false,height:48},tokens:{light:{colors:{container:"rgb(255, 255, 255)",layout:"rgb(247, 250, 252)","base-text":"rgb(31, 31, 31)"},boxShadow:{header:"0 1px 2px rgb(0, 21, 41, 0.08)",sider:"2px 0 8px 0 rgb(29, 35, 41, 0.05)"}},dark:{colors:{container:"rgb(28, 28, 28)",layout:"rgb(18, 18, 18)","base-text":"rgb(224, 224, 224)"}}}};
/* Override theme settings
 *
 * If publish new version, use `overrideThemeSettings` to override certain theme settings
 */
export const overrideThemeSettings: Partial<App.Theme.ThemeSetting> = {}
