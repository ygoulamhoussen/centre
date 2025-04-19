import{ax as ae,c6 as ne,az as o,z as se,C as v,B as x,ac as I,A as z,d as te,aA as N,D as y,bo as ie,O as de,E as he,H as U,J as ge,L as be,aC as Ce,a as D,aL as d,bF as ue,M as ve,c7 as L,ap as fe,N as pe}from"./index-D_-DzWhp.js";function ke(l){const{textColor2:h,primaryColorHover:r,primaryColorPressed:f,primaryColor:a,infoColor:i,successColor:s,warningColor:n,errorColor:t,baseColor:p,borderColor:k,opacityDisabled:g,tagColor:C,closeIconColor:e,closeIconColorHover:c,closeIconColorPressed:u,borderRadiusSmall:b,fontSizeMini:m,fontSizeTiny:S,fontSizeSmall:B,fontSizeMedium:H,heightMini:$,heightTiny:R,heightSmall:M,heightMedium:T,closeColorHover:E,closeColorPressed:O,buttonColor2Hover:_,buttonColor2Pressed:W,fontWeightStrong:j}=l;return Object.assign(Object.assign({},ne),{closeBorderRadius:b,heightTiny:$,heightSmall:R,heightMedium:M,heightLarge:T,borderRadius:b,opacityDisabled:g,fontSizeTiny:m,fontSizeSmall:S,fontSizeMedium:B,fontSizeLarge:H,fontWeightStrong:j,textColorCheckable:h,textColorHoverCheckable:h,textColorPressedCheckable:h,textColorChecked:p,colorCheckable:"#0000",colorHoverCheckable:_,colorPressedCheckable:W,colorChecked:a,colorCheckedHover:r,colorCheckedPressed:f,border:`1px solid ${k}`,textColor:h,color:C,colorBordered:"rgb(250, 250, 252)",closeIconColor:e,closeIconColorHover:c,closeIconColorPressed:u,closeColorHover:E,closeColorPressed:O,borderPrimary:`1px solid ${o(a,{alpha:.3})}`,textColorPrimary:a,colorPrimary:o(a,{alpha:.12}),colorBorderedPrimary:o(a,{alpha:.1}),closeIconColorPrimary:a,closeIconColorHoverPrimary:a,closeIconColorPressedPrimary:a,closeColorHoverPrimary:o(a,{alpha:.12}),closeColorPressedPrimary:o(a,{alpha:.18}),borderInfo:`1px solid ${o(i,{alpha:.3})}`,textColorInfo:i,colorInfo:o(i,{alpha:.12}),colorBorderedInfo:o(i,{alpha:.1}),closeIconColorInfo:i,closeIconColorHoverInfo:i,closeIconColorPressedInfo:i,closeColorHoverInfo:o(i,{alpha:.12}),closeColorPressedInfo:o(i,{alpha:.18}),borderSuccess:`1px solid ${o(s,{alpha:.3})}`,textColorSuccess:s,colorSuccess:o(s,{alpha:.12}),colorBorderedSuccess:o(s,{alpha:.1}),closeIconColorSuccess:s,closeIconColorHoverSuccess:s,closeIconColorPressedSuccess:s,closeColorHoverSuccess:o(s,{alpha:.12}),closeColorPressedSuccess:o(s,{alpha:.18}),borderWarning:`1px solid ${o(n,{alpha:.35})}`,textColorWarning:n,colorWarning:o(n,{alpha:.15}),colorBorderedWarning:o(n,{alpha:.12}),closeIconColorWarning:n,closeIconColorHoverWarning:n,closeIconColorPressedWarning:n,closeColorHoverWarning:o(n,{alpha:.12}),closeColorPressedWarning:o(n,{alpha:.18}),borderError:`1px solid ${o(t,{alpha:.23})}`,textColorError:t,colorError:o(t,{alpha:.1}),colorBorderedError:o(t,{alpha:.08}),closeIconColorError:t,closeIconColorHoverError:t,closeIconColorPressedError:t,closeColorHoverError:o(t,{alpha:.12}),closeColorPressedError:o(t,{alpha:.18})})}const me={common:ae,self:ke},xe={color:Object,type:{type:String,default:"default"},round:Boolean,size:{type:String,default:"medium"},closable:Boolean,disabled:{type:Boolean,default:void 0}},ye=se("tag",`
 --n-close-margin: var(--n-close-margin-top) var(--n-close-margin-right) var(--n-close-margin-bottom) var(--n-close-margin-left);
 white-space: nowrap;
 position: relative;
 box-sizing: border-box;
 cursor: default;
 display: inline-flex;
 align-items: center;
 flex-wrap: nowrap;
 padding: var(--n-padding);
 border-radius: var(--n-border-radius);
 color: var(--n-text-color);
 background-color: var(--n-color);
 transition: 
 border-color .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 opacity .3s var(--n-bezier);
 line-height: 1;
 height: var(--n-height);
 font-size: var(--n-font-size);
`,[v("strong",`
 font-weight: var(--n-font-weight-strong);
 `),x("border",`
 pointer-events: none;
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 border-radius: inherit;
 border: var(--n-border);
 transition: border-color .3s var(--n-bezier);
 `),x("icon",`
 display: flex;
 margin: 0 4px 0 0;
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 font-size: var(--n-avatar-size-override);
 `),x("avatar",`
 display: flex;
 margin: 0 6px 0 0;
 `),x("close",`
 margin: var(--n-close-margin);
 transition:
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 `),v("round",`
 padding: 0 calc(var(--n-height) / 3);
 border-radius: calc(var(--n-height) / 2);
 `,[x("icon",`
 margin: 0 4px 0 calc((var(--n-height) - 8px) / -2);
 `),x("avatar",`
 margin: 0 6px 0 calc((var(--n-height) - 8px) / -2);
 `),v("closable",`
 padding: 0 calc(var(--n-height) / 4) 0 calc(var(--n-height) / 3);
 `)]),v("icon, avatar",[v("round",`
 padding: 0 calc(var(--n-height) / 3) 0 calc(var(--n-height) / 2);
 `)]),v("disabled",`
 cursor: not-allowed !important;
 opacity: var(--n-opacity-disabled);
 `),v("checkable",`
 cursor: pointer;
 box-shadow: none;
 color: var(--n-text-color-checkable);
 background-color: var(--n-color-checkable);
 `,[I("disabled",[z("&:hover","background-color: var(--n-color-hover-checkable);",[I("checked","color: var(--n-text-color-hover-checkable);")]),z("&:active","background-color: var(--n-color-pressed-checkable);",[I("checked","color: var(--n-text-color-pressed-checkable);")])]),v("checked",`
 color: var(--n-text-color-checked);
 background-color: var(--n-color-checked);
 `,[I("disabled",[z("&:hover","background-color: var(--n-color-checked-hover);"),z("&:active","background-color: var(--n-color-checked-pressed);")])])])]),Pe=Object.assign(Object.assign(Object.assign({},U.props),xe),{bordered:{type:Boolean,default:void 0},checked:Boolean,checkable:Boolean,strong:Boolean,triggerClickOnClose:Boolean,onClose:[Array,Function],onMouseenter:Function,onMouseleave:Function,"onUpdate:checked":Function,onUpdateChecked:Function,internalCloseFocusable:{type:Boolean,default:!0},internalCloseIsButtonTag:{type:Boolean,default:!0},onCheckedChange:Function}),Ie=pe("n-tag"),Se=te({name:"Tag",props:Pe,slots:Object,setup(l){const h=de(null),{mergedBorderedRef:r,mergedClsPrefixRef:f,inlineThemeDisabled:a,mergedRtlRef:i}=he(l),s=U("Tag","-tag",ye,me,l,f);ge(Ie,{roundRef:be(l,"round")});function n(){if(!l.disabled&&l.checkable){const{checked:e,onCheckedChange:c,onUpdateChecked:u,"onUpdate:checked":b}=l;u&&u(!e),b&&b(!e),c&&c(!e)}}function t(e){if(l.triggerClickOnClose||e.stopPropagation(),!l.disabled){const{onClose:c}=l;c&&fe(c,e)}}const p={setTextContent(e){const{value:c}=h;c&&(c.textContent=e)}},k=Ce("Tag",i,f),g=D(()=>{const{type:e,size:c,color:{color:u,textColor:b}={}}=l,{common:{cubicBezierEaseInOut:m},self:{padding:S,closeMargin:B,borderRadius:H,opacityDisabled:$,textColorCheckable:R,textColorHoverCheckable:M,textColorPressedCheckable:T,textColorChecked:E,colorCheckable:O,colorHoverCheckable:_,colorPressedCheckable:W,colorChecked:j,colorCheckedHover:V,colorCheckedPressed:A,closeBorderRadius:K,fontWeightStrong:J,[d("colorBordered",e)]:q,[d("closeSize",c)]:G,[d("closeIconSize",c)]:Q,[d("fontSize",c)]:X,[d("height",c)]:w,[d("color",e)]:Y,[d("textColor",e)]:Z,[d("border",e)]:ee,[d("closeIconColor",e)]:F,[d("closeIconColorHover",e)]:oe,[d("closeIconColorPressed",e)]:re,[d("closeColorHover",e)]:le,[d("closeColorPressed",e)]:ce}}=s.value,P=ue(B);return{"--n-font-weight-strong":J,"--n-avatar-size-override":`calc(${w} - 8px)`,"--n-bezier":m,"--n-border-radius":H,"--n-border":ee,"--n-close-icon-size":Q,"--n-close-color-pressed":ce,"--n-close-color-hover":le,"--n-close-border-radius":K,"--n-close-icon-color":F,"--n-close-icon-color-hover":oe,"--n-close-icon-color-pressed":re,"--n-close-icon-color-disabled":F,"--n-close-margin-top":P.top,"--n-close-margin-right":P.right,"--n-close-margin-bottom":P.bottom,"--n-close-margin-left":P.left,"--n-close-size":G,"--n-color":u||(r.value?q:Y),"--n-color-checkable":O,"--n-color-checked":j,"--n-color-checked-hover":V,"--n-color-checked-pressed":A,"--n-color-hover-checkable":_,"--n-color-pressed-checkable":W,"--n-font-size":X,"--n-height":w,"--n-opacity-disabled":$,"--n-padding":S,"--n-text-color":b||Z,"--n-text-color-checkable":R,"--n-text-color-checked":E,"--n-text-color-hover-checkable":M,"--n-text-color-pressed-checkable":T}}),C=a?ve("tag",D(()=>{let e="";const{type:c,size:u,color:{color:b,textColor:m}={}}=l;return e+=c[0],e+=u[0],b&&(e+=`a${L(b)}`),m&&(e+=`b${L(m)}`),r.value&&(e+="c"),e}),g,l):void 0;return Object.assign(Object.assign({},p),{rtlEnabled:k,mergedClsPrefix:f,contentRef:h,mergedBordered:r,handleClick:n,handleCloseClick:t,cssVars:a?void 0:g,themeClass:C==null?void 0:C.themeClass,onRender:C==null?void 0:C.onRender})},render(){var l,h;const{mergedClsPrefix:r,rtlEnabled:f,closable:a,color:{borderColor:i}={},round:s,onRender:n,$slots:t}=this;n==null||n();const p=N(t.avatar,g=>g&&y("div",{class:`${r}-tag__avatar`},g)),k=N(t.icon,g=>g&&y("div",{class:`${r}-tag__icon`},g));return y("div",{class:[`${r}-tag`,this.themeClass,{[`${r}-tag--rtl`]:f,[`${r}-tag--strong`]:this.strong,[`${r}-tag--disabled`]:this.disabled,[`${r}-tag--checkable`]:this.checkable,[`${r}-tag--checked`]:this.checkable&&this.checked,[`${r}-tag--round`]:s,[`${r}-tag--avatar`]:p,[`${r}-tag--icon`]:k,[`${r}-tag--closable`]:a}],style:this.cssVars,onClick:this.handleClick,onMouseenter:this.onMouseenter,onMouseleave:this.onMouseleave},k||p,y("span",{class:`${r}-tag__content`,ref:"contentRef"},(h=(l=this.$slots).default)===null||h===void 0?void 0:h.call(l)),!this.checkable&&a?y(ie,{clsPrefix:r,class:`${r}-tag__close`,disabled:this.disabled,onClick:this.handleCloseClick,focusable:this.internalCloseFocusable,round:s,isButtonTag:this.internalCloseIsButtonTag,absolute:!0}):null,!this.checkable&&this.mergedBordered?y("div",{class:`${r}-tag__border`,style:{borderColor:i}}):null)}});export{Se as N};
