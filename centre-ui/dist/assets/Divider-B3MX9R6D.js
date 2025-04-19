import{z as f,ac as c,B as r,C as n,d as m,D as o,F as p,E as b,H as h,bz as x,a as _,M as C}from"./index-D_-DzWhp.js";const $=f("divider",`
 position: relative;
 display: flex;
 width: 100%;
 box-sizing: border-box;
 font-size: 16px;
 color: var(--n-text-color);
 transition:
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
`,[c("vertical",`
 margin-top: 24px;
 margin-bottom: 24px;
 `,[c("no-title",`
 display: flex;
 align-items: center;
 `)]),r("title",`
 display: flex;
 align-items: center;
 margin-left: 12px;
 margin-right: 12px;
 white-space: nowrap;
 font-weight: var(--n-font-weight);
 `),n("title-position-left",[r("line",[n("left",{width:"28px"})])]),n("title-position-right",[r("line",[n("right",{width:"28px"})])]),n("dashed",[r("line",`
 background-color: #0000;
 height: 0px;
 width: 100%;
 border-style: dashed;
 border-width: 1px 0 0;
 `)]),n("vertical",`
 display: inline-block;
 height: 1em;
 margin: 0 8px;
 vertical-align: middle;
 width: 1px;
 `),r("line",`
 border: none;
 transition: background-color .3s var(--n-bezier), border-color .3s var(--n-bezier);
 height: 1px;
 width: 100%;
 margin: 0;
 `),c("dashed",[r("line",{backgroundColor:"var(--n-color)"})]),n("dashed",[r("line",{borderColor:"var(--n-color)"})]),n("vertical",{backgroundColor:"var(--n-color)"})]),w=Object.assign(Object.assign({},h.props),{titlePlacement:{type:String,default:"center"},dashed:Boolean,vertical:Boolean}),y=m({name:"Divider",props:w,setup(t){const{mergedClsPrefixRef:d,inlineThemeDisabled:l}=b(t),s=h("Divider","-divider",$,x,t,d),a=_(()=>{const{common:{cubicBezierEaseInOut:e},self:{color:v,textColor:u,fontWeight:g}}=s.value;return{"--n-bezier":e,"--n-color":v,"--n-text-color":u,"--n-font-weight":g}}),i=l?C("divider",void 0,a,t):void 0;return{mergedClsPrefix:d,cssVars:l?void 0:a,themeClass:i==null?void 0:i.themeClass,onRender:i==null?void 0:i.onRender}},render(){var t;const{$slots:d,titlePlacement:l,vertical:s,dashed:a,cssVars:i,mergedClsPrefix:e}=this;return(t=this.onRender)===null||t===void 0||t.call(this),o("div",{role:"separator",class:[`${e}-divider`,this.themeClass,{[`${e}-divider--vertical`]:s,[`${e}-divider--no-title`]:!d.default,[`${e}-divider--dashed`]:a,[`${e}-divider--title-position-${l}`]:d.default&&l}],style:i},s?null:o("div",{class:`${e}-divider__line ${e}-divider__line--left`}),!s&&d.default?o(p,null,o("div",{class:`${e}-divider__title`},this.$slots),o("div",{class:`${e}-divider__line ${e}-divider__line--right`})):null)}});export{y as _};
