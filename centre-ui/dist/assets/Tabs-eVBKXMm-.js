import{d as se,D as l,O as $,bA as lt,bB as dt,N as bt,ao as ct,ae as ft,F as pt,af as ut,bo as vt,S as ht,a as q,bC as gt,z as r,C as o,A as u,B as y,ac as xt,bD as ee,aA as ve,ak as te,E as mt,H as we,bE as yt,a5 as ae,P as wt,J as St,L as E,am as Ct,aL as j,bF as Y,M as Rt,aN as zt,w as $t,aS as Tt,bG as Pt,aF as re,v as Wt,ap as K}from"./index-D_-DzWhp.js";import{A as _t}from"./Add-CIDYJCEd.js";import{c as he,a as Lt,o as At}from"./cssr-BW6v_MNg.js";import{u as ge}from"./use-compitable-Cr7qxnTd.js";import{u as Bt}from"./use-merged-state-DuV1Lu3L.js";import{d as Et}from"./debounce-Dhm6kdwJ.js";const jt=he(".v-x-scroll",{overflow:"auto",scrollbarWidth:"none"},[he("&::-webkit-scrollbar",{width:0,height:0})]),kt=se({name:"XScroll",props:{disabled:Boolean,onScroll:Function},setup(){const e=$(null);function s(d){!(d.currentTarget.offsetWidth<d.currentTarget.scrollWidth)||d.deltaY===0||(d.currentTarget.scrollLeft+=d.deltaY+d.deltaX,d.preventDefault())}const b=lt();return jt.mount({id:"vueuc/x-scroll",head:!0,anchorMetaName:Lt,ssr:b}),Object.assign({selfRef:e,handleWheel:s},{scrollTo(...d){var w;(w=e.value)===null||w===void 0||w.scrollTo(...d)}})},render(){return l("div",{ref:"selfRef",onScroll:this.onScroll,onWheel:this.disabled?void 0:this.handleWheel,class:"v-x-scroll"},this.$slots)}});var Ht="Expected a function";function ne(e,s,b){var v=!0,d=!0;if(typeof e!="function")throw new TypeError(Ht);return dt(b)&&(v="leading"in b?!!b.leading:v,d="trailing"in b?!!b.trailing:d),Et(e,s,{leading:v,maxWait:s,trailing:d})}const Se=bt("n-tabs"),Ot={tab:[String,Number,Object,Function],name:{type:[String,Number],required:!0},disabled:Boolean,displayDirective:{type:String,default:"if"},closable:{type:Boolean,default:void 0},tabProps:Object,label:[String,Number,Object,Function]},Ft=Object.assign({internalLeftPadded:Boolean,internalAddable:Boolean,internalCreatedByPane:Boolean},gt(Ot,["displayDirective"])),ie=se({__TAB__:!0,inheritAttrs:!1,name:"Tab",props:Ft,setup(e){const{mergedClsPrefixRef:s,valueRef:b,typeRef:v,closableRef:d,tabStyleRef:w,addTabStyleRef:h,tabClassRef:S,addTabClassRef:C,tabChangeIdRef:g,onBeforeLeaveRef:f,triggerRef:k,handleAdd:A,activateTab:x,handleClose:R}=ht(Se);return{trigger:k,mergedClosable:q(()=>{if(e.internalAddable)return!1;const{closable:m}=e;return m===void 0?d.value:m}),style:w,addStyle:h,tabClass:S,addTabClass:C,clsPrefix:s,value:b,type:v,handleClose(m){m.stopPropagation(),!e.disabled&&R(e.name)},activateTab(){if(e.disabled)return;if(e.internalAddable){A();return}const{name:m}=e,P=++g.id;if(m!==b.value){const{value:B}=f;B?Promise.resolve(B(e.name,b.value)).then(T=>{T&&g.id===P&&x(m)}):x(m)}}}},render(){const{internalAddable:e,clsPrefix:s,name:b,disabled:v,label:d,tab:w,value:h,mergedClosable:S,trigger:C,$slots:{default:g}}=this,f=d??w;return l("div",{class:`${s}-tabs-tab-wrapper`},this.internalLeftPadded?l("div",{class:`${s}-tabs-tab-pad`}):null,l("div",Object.assign({key:b,"data-name":b,"data-disabled":v?!0:void 0},ct({class:[`${s}-tabs-tab`,h===b&&`${s}-tabs-tab--active`,v&&`${s}-tabs-tab--disabled`,S&&`${s}-tabs-tab--closable`,e&&`${s}-tabs-tab--addable`,e?this.addTabClass:this.tabClass],onClick:C==="click"?this.activateTab:void 0,onMouseenter:C==="hover"?this.activateTab:void 0,style:e?this.addStyle:this.style},this.internalCreatedByPane?this.tabProps||{}:this.$attrs)),l("span",{class:`${s}-tabs-tab__label`},e?l(pt,null,l("div",{class:`${s}-tabs-tab__height-placeholder`}," "),l(ut,{clsPrefix:s},{default:()=>l(_t,null)})):g?g():typeof f=="object"?f:ft(f??b)),S&&this.type==="card"?l(vt,{clsPrefix:s,class:`${s}-tabs-tab__close`,onClick:this.handleClose,disabled:v}):null))}}),Dt=r("tabs",`
 box-sizing: border-box;
 width: 100%;
 display: flex;
 flex-direction: column;
 transition:
 background-color .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
`,[o("segment-type",[r("tabs-rail",[u("&.transition-disabled",[r("tabs-capsule",`
 transition: none;
 `)])])]),o("top",[r("tab-pane",`
 padding: var(--n-pane-padding-top) var(--n-pane-padding-right) var(--n-pane-padding-bottom) var(--n-pane-padding-left);
 `)]),o("left",[r("tab-pane",`
 padding: var(--n-pane-padding-right) var(--n-pane-padding-bottom) var(--n-pane-padding-left) var(--n-pane-padding-top);
 `)]),o("left, right",`
 flex-direction: row;
 `,[r("tabs-bar",`
 width: 2px;
 right: 0;
 transition:
 top .2s var(--n-bezier),
 max-height .2s var(--n-bezier),
 background-color .3s var(--n-bezier);
 `),r("tabs-tab",`
 padding: var(--n-tab-padding-vertical); 
 `)]),o("right",`
 flex-direction: row-reverse;
 `,[r("tab-pane",`
 padding: var(--n-pane-padding-left) var(--n-pane-padding-top) var(--n-pane-padding-right) var(--n-pane-padding-bottom);
 `),r("tabs-bar",`
 left: 0;
 `)]),o("bottom",`
 flex-direction: column-reverse;
 justify-content: flex-end;
 `,[r("tab-pane",`
 padding: var(--n-pane-padding-bottom) var(--n-pane-padding-right) var(--n-pane-padding-top) var(--n-pane-padding-left);
 `),r("tabs-bar",`
 top: 0;
 `)]),r("tabs-rail",`
 position: relative;
 padding: 3px;
 border-radius: var(--n-tab-border-radius);
 width: 100%;
 background-color: var(--n-color-segment);
 transition: background-color .3s var(--n-bezier);
 display: flex;
 align-items: center;
 `,[r("tabs-capsule",`
 border-radius: var(--n-tab-border-radius);
 position: absolute;
 pointer-events: none;
 background-color: var(--n-tab-color-segment);
 box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .08);
 transition: transform 0.3s var(--n-bezier);
 `),r("tabs-tab-wrapper",`
 flex-basis: 0;
 flex-grow: 1;
 display: flex;
 align-items: center;
 justify-content: center;
 `,[r("tabs-tab",`
 overflow: hidden;
 border-radius: var(--n-tab-border-radius);
 width: 100%;
 display: flex;
 align-items: center;
 justify-content: center;
 `,[o("active",`
 font-weight: var(--n-font-weight-strong);
 color: var(--n-tab-text-color-active);
 `),u("&:hover",`
 color: var(--n-tab-text-color-hover);
 `)])])]),o("flex",[r("tabs-nav",`
 width: 100%;
 position: relative;
 `,[r("tabs-wrapper",`
 width: 100%;
 `,[r("tabs-tab",`
 margin-right: 0;
 `)])])]),r("tabs-nav",`
 box-sizing: border-box;
 line-height: 1.5;
 display: flex;
 transition: border-color .3s var(--n-bezier);
 `,[y("prefix, suffix",`
 display: flex;
 align-items: center;
 `),y("prefix","padding-right: 16px;"),y("suffix","padding-left: 16px;")]),o("top, bottom",[r("tabs-nav-scroll-wrapper",[u("&::before",`
 top: 0;
 bottom: 0;
 left: 0;
 width: 20px;
 `),u("&::after",`
 top: 0;
 bottom: 0;
 right: 0;
 width: 20px;
 `),o("shadow-start",[u("&::before",`
 box-shadow: inset 10px 0 8px -8px rgba(0, 0, 0, .12);
 `)]),o("shadow-end",[u("&::after",`
 box-shadow: inset -10px 0 8px -8px rgba(0, 0, 0, .12);
 `)])])]),o("left, right",[r("tabs-nav-scroll-content",`
 flex-direction: column;
 `),r("tabs-nav-scroll-wrapper",[u("&::before",`
 top: 0;
 left: 0;
 right: 0;
 height: 20px;
 `),u("&::after",`
 bottom: 0;
 left: 0;
 right: 0;
 height: 20px;
 `),o("shadow-start",[u("&::before",`
 box-shadow: inset 0 10px 8px -8px rgba(0, 0, 0, .12);
 `)]),o("shadow-end",[u("&::after",`
 box-shadow: inset 0 -10px 8px -8px rgba(0, 0, 0, .12);
 `)])])]),r("tabs-nav-scroll-wrapper",`
 flex: 1;
 position: relative;
 overflow: hidden;
 `,[r("tabs-nav-y-scroll",`
 height: 100%;
 width: 100%;
 overflow-y: auto; 
 scrollbar-width: none;
 `,[u("&::-webkit-scrollbar, &::-webkit-scrollbar-track-piece, &::-webkit-scrollbar-thumb",`
 width: 0;
 height: 0;
 display: none;
 `)]),u("&::before, &::after",`
 transition: box-shadow .3s var(--n-bezier);
 pointer-events: none;
 content: "";
 position: absolute;
 z-index: 1;
 `)]),r("tabs-nav-scroll-content",`
 display: flex;
 position: relative;
 min-width: 100%;
 min-height: 100%;
 width: fit-content;
 box-sizing: border-box;
 `),r("tabs-wrapper",`
 display: inline-flex;
 flex-wrap: nowrap;
 position: relative;
 `),r("tabs-tab-wrapper",`
 display: flex;
 flex-wrap: nowrap;
 flex-shrink: 0;
 flex-grow: 0;
 `),r("tabs-tab",`
 cursor: pointer;
 white-space: nowrap;
 flex-wrap: nowrap;
 display: inline-flex;
 align-items: center;
 color: var(--n-tab-text-color);
 font-size: var(--n-tab-font-size);
 background-clip: padding-box;
 padding: var(--n-tab-padding);
 transition:
 box-shadow .3s var(--n-bezier),
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 `,[o("disabled",{cursor:"not-allowed"}),y("close",`
 margin-left: 6px;
 transition:
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 `),y("label",`
 display: flex;
 align-items: center;
 z-index: 1;
 `)]),r("tabs-bar",`
 position: absolute;
 bottom: 0;
 height: 2px;
 border-radius: 1px;
 background-color: var(--n-bar-color);
 transition:
 left .2s var(--n-bezier),
 max-width .2s var(--n-bezier),
 opacity .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 `,[u("&.transition-disabled",`
 transition: none;
 `),o("disabled",`
 background-color: var(--n-tab-text-color-disabled)
 `)]),r("tabs-pane-wrapper",`
 position: relative;
 overflow: hidden;
 transition: max-height .2s var(--n-bezier);
 `),r("tab-pane",`
 color: var(--n-pane-text-color);
 width: 100%;
 transition:
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 opacity .2s var(--n-bezier);
 left: 0;
 right: 0;
 top: 0;
 `,[u("&.next-transition-leave-active, &.prev-transition-leave-active, &.next-transition-enter-active, &.prev-transition-enter-active",`
 transition:
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 transform .2s var(--n-bezier),
 opacity .2s var(--n-bezier);
 `),u("&.next-transition-leave-active, &.prev-transition-leave-active",`
 position: absolute;
 `),u("&.next-transition-enter-from, &.prev-transition-leave-to",`
 transform: translateX(32px);
 opacity: 0;
 `),u("&.next-transition-leave-to, &.prev-transition-enter-from",`
 transform: translateX(-32px);
 opacity: 0;
 `),u("&.next-transition-leave-from, &.next-transition-enter-to, &.prev-transition-leave-from, &.prev-transition-enter-to",`
 transform: translateX(0);
 opacity: 1;
 `)]),r("tabs-tab-pad",`
 box-sizing: border-box;
 width: var(--n-tab-gap);
 flex-grow: 0;
 flex-shrink: 0;
 `),o("line-type, bar-type",[r("tabs-tab",`
 font-weight: var(--n-tab-font-weight);
 box-sizing: border-box;
 vertical-align: bottom;
 `,[u("&:hover",{color:"var(--n-tab-text-color-hover)"}),o("active",`
 color: var(--n-tab-text-color-active);
 font-weight: var(--n-tab-font-weight-active);
 `),o("disabled",{color:"var(--n-tab-text-color-disabled)"})])]),r("tabs-nav",[o("line-type",[o("top",[y("prefix, suffix",`
 border-bottom: 1px solid var(--n-tab-border-color);
 `),r("tabs-nav-scroll-content",`
 border-bottom: 1px solid var(--n-tab-border-color);
 `),r("tabs-bar",`
 bottom: -1px;
 `)]),o("left",[y("prefix, suffix",`
 border-right: 1px solid var(--n-tab-border-color);
 `),r("tabs-nav-scroll-content",`
 border-right: 1px solid var(--n-tab-border-color);
 `),r("tabs-bar",`
 right: -1px;
 `)]),o("right",[y("prefix, suffix",`
 border-left: 1px solid var(--n-tab-border-color);
 `),r("tabs-nav-scroll-content",`
 border-left: 1px solid var(--n-tab-border-color);
 `),r("tabs-bar",`
 left: -1px;
 `)]),o("bottom",[y("prefix, suffix",`
 border-top: 1px solid var(--n-tab-border-color);
 `),r("tabs-nav-scroll-content",`
 border-top: 1px solid var(--n-tab-border-color);
 `),r("tabs-bar",`
 top: -1px;
 `)]),y("prefix, suffix",`
 transition: border-color .3s var(--n-bezier);
 `),r("tabs-nav-scroll-content",`
 transition: border-color .3s var(--n-bezier);
 `),r("tabs-bar",`
 border-radius: 0;
 `)]),o("card-type",[y("prefix, suffix",`
 transition: border-color .3s var(--n-bezier);
 `),r("tabs-pad",`
 flex-grow: 1;
 transition: border-color .3s var(--n-bezier);
 `),r("tabs-tab-pad",`
 transition: border-color .3s var(--n-bezier);
 `),r("tabs-tab",`
 font-weight: var(--n-tab-font-weight);
 border: 1px solid var(--n-tab-border-color);
 background-color: var(--n-tab-color);
 box-sizing: border-box;
 position: relative;
 vertical-align: bottom;
 display: flex;
 justify-content: space-between;
 font-size: var(--n-tab-font-size);
 color: var(--n-tab-text-color);
 `,[o("addable",`
 padding-left: 8px;
 padding-right: 8px;
 font-size: 16px;
 justify-content: center;
 `,[y("height-placeholder",`
 width: 0;
 font-size: var(--n-tab-font-size);
 `),xt("disabled",[u("&:hover",`
 color: var(--n-tab-text-color-hover);
 `)])]),o("closable","padding-right: 8px;"),o("active",`
 background-color: #0000;
 font-weight: var(--n-tab-font-weight-active);
 color: var(--n-tab-text-color-active);
 `),o("disabled","color: var(--n-tab-text-color-disabled);")])]),o("left, right",`
 flex-direction: column; 
 `,[y("prefix, suffix",`
 padding: var(--n-tab-padding-vertical);
 `),r("tabs-wrapper",`
 flex-direction: column;
 `),r("tabs-tab-wrapper",`
 flex-direction: column;
 `,[r("tabs-tab-pad",`
 height: var(--n-tab-gap-vertical);
 width: 100%;
 `)])]),o("top",[o("card-type",[r("tabs-scroll-padding","border-bottom: 1px solid var(--n-tab-border-color);"),y("prefix, suffix",`
 border-bottom: 1px solid var(--n-tab-border-color);
 `),r("tabs-tab",`
 border-top-left-radius: var(--n-tab-border-radius);
 border-top-right-radius: var(--n-tab-border-radius);
 `,[o("active",`
 border-bottom: 1px solid #0000;
 `)]),r("tabs-tab-pad",`
 border-bottom: 1px solid var(--n-tab-border-color);
 `),r("tabs-pad",`
 border-bottom: 1px solid var(--n-tab-border-color);
 `)])]),o("left",[o("card-type",[r("tabs-scroll-padding","border-right: 1px solid var(--n-tab-border-color);"),y("prefix, suffix",`
 border-right: 1px solid var(--n-tab-border-color);
 `),r("tabs-tab",`
 border-top-left-radius: var(--n-tab-border-radius);
 border-bottom-left-radius: var(--n-tab-border-radius);
 `,[o("active",`
 border-right: 1px solid #0000;
 `)]),r("tabs-tab-pad",`
 border-right: 1px solid var(--n-tab-border-color);
 `),r("tabs-pad",`
 border-right: 1px solid var(--n-tab-border-color);
 `)])]),o("right",[o("card-type",[r("tabs-scroll-padding","border-left: 1px solid var(--n-tab-border-color);"),y("prefix, suffix",`
 border-left: 1px solid var(--n-tab-border-color);
 `),r("tabs-tab",`
 border-top-right-radius: var(--n-tab-border-radius);
 border-bottom-right-radius: var(--n-tab-border-radius);
 `,[o("active",`
 border-left: 1px solid #0000;
 `)]),r("tabs-tab-pad",`
 border-left: 1px solid var(--n-tab-border-color);
 `),r("tabs-pad",`
 border-left: 1px solid var(--n-tab-border-color);
 `)])]),o("bottom",[o("card-type",[r("tabs-scroll-padding","border-top: 1px solid var(--n-tab-border-color);"),y("prefix, suffix",`
 border-top: 1px solid var(--n-tab-border-color);
 `),r("tabs-tab",`
 border-bottom-left-radius: var(--n-tab-border-radius);
 border-bottom-right-radius: var(--n-tab-border-radius);
 `,[o("active",`
 border-top: 1px solid #0000;
 `)]),r("tabs-tab-pad",`
 border-top: 1px solid var(--n-tab-border-color);
 `),r("tabs-pad",`
 border-top: 1px solid var(--n-tab-border-color);
 `)])])])]),It=Object.assign(Object.assign({},we.props),{value:[String,Number],defaultValue:[String,Number],trigger:{type:String,default:"click"},type:{type:String,default:"bar"},closable:Boolean,justifyContent:String,size:{type:String,default:"medium"},placement:{type:String,default:"top"},tabStyle:[String,Object],tabClass:String,addTabStyle:[String,Object],addTabClass:String,barWidth:Number,paneClass:String,paneStyle:[String,Object],paneWrapperClass:String,paneWrapperStyle:[String,Object],addable:[Boolean,Object],tabsPadding:{type:Number,default:0},animated:Boolean,onBeforeLeave:Function,onAdd:Function,"onUpdate:value":[Function,Array],onUpdateValue:[Function,Array],onClose:[Function,Array],labelSize:String,activeName:[String,Number],onActiveNameChange:[Function,Array]}),Yt=se({name:"Tabs",props:It,slots:Object,setup(e,{slots:s}){var b,v,d,w;const{mergedClsPrefixRef:h,inlineThemeDisabled:S}=mt(e),C=we("Tabs","-tabs",Dt,yt,e,h),g=$(null),f=$(null),k=$(null),A=$(null),x=$(null),R=$(null),m=$(!0),P=$(!0),B=ge(e,["labelSize","size"]),T=ge(e,["activeName","value"]),V=$((v=(b=T.value)!==null&&b!==void 0?b:e.defaultValue)!==null&&v!==void 0?v:s.default?(w=(d=ee(s.default())[0])===null||d===void 0?void 0:d.props)===null||w===void 0?void 0:w.name:null),W=Bt(T,V),c={id:0},z=q(()=>{if(!(!e.justifyContent||e.type==="card"))return{display:"flex",justifyContent:e.justifyContent}});ae(W,()=>{c.id=0,X(),de()});function H(){var t;const{value:a}=W;return a===null?null:(t=g.value)===null||t===void 0?void 0:t.querySelector(`[data-name="${a}"]`)}function Ce(t){if(e.type==="card")return;const{value:a}=f;if(!a)return;const n=a.style.opacity==="0";if(t){const i=`${h.value}-tabs-bar--disabled`,{barWidth:p,placement:_}=e;if(t.dataset.disabled==="true"?a.classList.add(i):a.classList.remove(i),["top","bottom"].includes(_)){if(le(["top","maxHeight","height"]),typeof p=="number"&&t.offsetWidth>=p){const L=Math.floor((t.offsetWidth-p)/2)+t.offsetLeft;a.style.left=`${L}px`,a.style.maxWidth=`${p}px`}else a.style.left=`${t.offsetLeft}px`,a.style.maxWidth=`${t.offsetWidth}px`;a.style.width="8192px",n&&(a.style.transition="none"),a.offsetWidth,n&&(a.style.transition="",a.style.opacity="1")}else{if(le(["left","maxWidth","width"]),typeof p=="number"&&t.offsetHeight>=p){const L=Math.floor((t.offsetHeight-p)/2)+t.offsetTop;a.style.top=`${L}px`,a.style.maxHeight=`${p}px`}else a.style.top=`${t.offsetTop}px`,a.style.maxHeight=`${t.offsetHeight}px`;a.style.height="8192px",n&&(a.style.transition="none"),a.offsetHeight,n&&(a.style.transition="",a.style.opacity="1")}}}function Re(){if(e.type==="card")return;const{value:t}=f;t&&(t.style.opacity="0")}function le(t){const{value:a}=f;if(a)for(const n of t)a.style[n]=""}function X(){if(e.type==="card")return;const t=H();t?Ce(t):Re()}function de(){var t;const a=(t=x.value)===null||t===void 0?void 0:t.$el;if(!a)return;const n=H();if(!n)return;const{scrollLeft:i,offsetWidth:p}=a,{offsetLeft:_,offsetWidth:L}=n;i>_?a.scrollTo({top:0,left:_,behavior:"smooth"}):_+L>i+p&&a.scrollTo({top:0,left:_+L-p,behavior:"smooth"})}const U=$(null);let J=0,O=null;function ze(t){const a=U.value;if(a){J=t.getBoundingClientRect().height;const n=`${J}px`,i=()=>{a.style.height=n,a.style.maxHeight=n};O?(i(),O(),O=null):O=i}}function $e(t){const a=U.value;if(a){const n=t.getBoundingClientRect().height,i=()=>{document.body.offsetHeight,a.style.maxHeight=`${n}px`,a.style.height=`${Math.max(J,n)}px`};O?(O(),O=null,i()):O=i}}function Te(){const t=U.value;if(t){t.style.maxHeight="",t.style.height="";const{paneWrapperStyle:a}=e;if(typeof a=="string")t.style.cssText=a;else if(a){const{maxHeight:n,height:i}=a;n!==void 0&&(t.style.maxHeight=n),i!==void 0&&(t.style.height=i)}}}const be={value:[]},ce=$("next");function Pe(t){const a=W.value;let n="next";for(const i of be.value){if(i===a)break;if(i===t){n="prev";break}}ce.value=n,We(t)}function We(t){const{onActiveNameChange:a,onUpdateValue:n,"onUpdate:value":i}=e;a&&K(a,t),n&&K(n,t),i&&K(i,t),V.value=t}function _e(t){const{onClose:a}=e;a&&K(a,t)}function fe(){const{value:t}=f;if(!t)return;const a="transition-disabled";t.classList.add(a),X(),t.classList.remove(a)}const F=$(null);function Q({transitionDisabled:t}){const a=g.value;if(!a)return;t&&a.classList.add("transition-disabled");const n=H();n&&F.value&&(F.value.style.width=`${n.offsetWidth}px`,F.value.style.height=`${n.offsetHeight}px`,F.value.style.transform=`translateX(${n.offsetLeft-zt(getComputedStyle(a).paddingLeft)}px)`,t&&F.value.offsetWidth),t&&a.classList.remove("transition-disabled")}ae([W],()=>{e.type==="segment"&&re(()=>{Q({transitionDisabled:!1})})}),wt(()=>{e.type==="segment"&&Q({transitionDisabled:!0})});let pe=0;function Le(t){var a;if(t.contentRect.width===0&&t.contentRect.height===0||pe===t.contentRect.width)return;pe=t.contentRect.width;const{type:n}=e;if((n==="line"||n==="bar")&&fe(),n!=="segment"){const{placement:i}=e;Z((i==="top"||i==="bottom"?(a=x.value)===null||a===void 0?void 0:a.$el:R.value)||null)}}const Ae=ne(Le,64);ae([()=>e.justifyContent,()=>e.size],()=>{re(()=>{const{type:t}=e;(t==="line"||t==="bar")&&fe()})});const D=$(!1);function Be(t){var a;const{target:n,contentRect:{width:i,height:p}}=t,_=n.parentElement.parentElement.offsetWidth,L=n.parentElement.parentElement.offsetHeight,{placement:M}=e;if(!D.value)M==="top"||M==="bottom"?_<i&&(D.value=!0):L<p&&(D.value=!0);else{const{value:N}=A;if(!N)return;M==="top"||M==="bottom"?_-i>N.$el.offsetWidth&&(D.value=!1):L-p>N.$el.offsetHeight&&(D.value=!1)}Z(((a=x.value)===null||a===void 0?void 0:a.$el)||null)}const Ee=ne(Be,64);function je(){const{onAdd:t}=e;t&&t(),re(()=>{const a=H(),{value:n}=x;!a||!n||n.scrollTo({left:a.offsetLeft,top:0,behavior:"smooth"})})}function Z(t){if(!t)return;const{placement:a}=e;if(a==="top"||a==="bottom"){const{scrollLeft:n,scrollWidth:i,offsetWidth:p}=t;m.value=n<=0,P.value=n+p>=i}else{const{scrollTop:n,scrollHeight:i,offsetHeight:p}=t;m.value=n<=0,P.value=n+p>=i}}const ke=ne(t=>{Z(t.target)},64);St(Se,{triggerRef:E(e,"trigger"),tabStyleRef:E(e,"tabStyle"),tabClassRef:E(e,"tabClass"),addTabStyleRef:E(e,"addTabStyle"),addTabClassRef:E(e,"addTabClass"),paneClassRef:E(e,"paneClass"),paneStyleRef:E(e,"paneStyle"),mergedClsPrefixRef:h,typeRef:E(e,"type"),closableRef:E(e,"closable"),valueRef:W,tabChangeIdRef:c,onBeforeLeaveRef:E(e,"onBeforeLeave"),activateTab:Pe,handleClose:_e,handleAdd:je}),At(()=>{X(),de()}),Ct(()=>{const{value:t}=k;if(!t)return;const{value:a}=h,n=`${a}-tabs-nav-scroll-wrapper--shadow-start`,i=`${a}-tabs-nav-scroll-wrapper--shadow-end`;m.value?t.classList.remove(n):t.classList.add(n),P.value?t.classList.remove(i):t.classList.add(i)});const He={syncBarPosition:()=>{X()}},Oe=()=>{Q({transitionDisabled:!0})},ue=q(()=>{const{value:t}=B,{type:a}=e,n={card:"Card",bar:"Bar",line:"Line",segment:"Segment"}[a],i=`${t}${n}`,{self:{barColor:p,closeIconColor:_,closeIconColorHover:L,closeIconColorPressed:M,tabColor:N,tabBorderColor:Fe,paneTextColor:De,tabFontWeight:Ie,tabBorderRadius:Me,tabFontWeightActive:Ve,colorSegment:Ne,fontWeightStrong:Xe,tabColorSegment:Ue,closeSize:Ge,closeIconSize:Ye,closeColorHover:Ke,closeColorPressed:qe,closeBorderRadius:Je,[j("panePadding",t)]:G,[j("tabPadding",i)]:Qe,[j("tabPaddingVertical",i)]:Ze,[j("tabGap",i)]:et,[j("tabGap",`${i}Vertical`)]:tt,[j("tabTextColor",a)]:at,[j("tabTextColorActive",a)]:rt,[j("tabTextColorHover",a)]:nt,[j("tabTextColorDisabled",a)]:ot,[j("tabFontSize",t)]:it},common:{cubicBezierEaseInOut:st}}=C.value;return{"--n-bezier":st,"--n-color-segment":Ne,"--n-bar-color":p,"--n-tab-font-size":it,"--n-tab-text-color":at,"--n-tab-text-color-active":rt,"--n-tab-text-color-disabled":ot,"--n-tab-text-color-hover":nt,"--n-pane-text-color":De,"--n-tab-border-color":Fe,"--n-tab-border-radius":Me,"--n-close-size":Ge,"--n-close-icon-size":Ye,"--n-close-color-hover":Ke,"--n-close-color-pressed":qe,"--n-close-border-radius":Je,"--n-close-icon-color":_,"--n-close-icon-color-hover":L,"--n-close-icon-color-pressed":M,"--n-tab-color":N,"--n-tab-font-weight":Ie,"--n-tab-font-weight-active":Ve,"--n-tab-padding":Qe,"--n-tab-padding-vertical":Ze,"--n-tab-gap":et,"--n-tab-gap-vertical":tt,"--n-pane-padding-left":Y(G,"left"),"--n-pane-padding-right":Y(G,"right"),"--n-pane-padding-top":Y(G,"top"),"--n-pane-padding-bottom":Y(G,"bottom"),"--n-font-weight-strong":Xe,"--n-tab-color-segment":Ue}}),I=S?Rt("tabs",q(()=>`${B.value[0]}${e.type[0]}`),ue,e):void 0;return Object.assign({mergedClsPrefix:h,mergedValue:W,renderedNames:new Set,segmentCapsuleElRef:F,tabsPaneWrapperRef:U,tabsElRef:g,barElRef:f,addTabInstRef:A,xScrollInstRef:x,scrollWrapperElRef:k,addTabFixed:D,tabWrapperStyle:z,handleNavResize:Ae,mergedSize:B,handleScroll:ke,handleTabsResize:Ee,cssVars:S?void 0:ue,themeClass:I==null?void 0:I.themeClass,animationDirection:ce,renderNameListRef:be,yScrollElRef:R,handleSegmentResize:Oe,onAnimationBeforeLeave:ze,onAnimationEnter:$e,onAnimationAfterEnter:Te,onRender:I==null?void 0:I.onRender},He)},render(){const{mergedClsPrefix:e,type:s,placement:b,addTabFixed:v,addable:d,mergedSize:w,renderNameListRef:h,onRender:S,paneWrapperClass:C,paneWrapperStyle:g,$slots:{default:f,prefix:k,suffix:A}}=this;S==null||S();const x=f?ee(f()).filter(c=>c.type.__TAB_PANE__===!0):[],R=f?ee(f()).filter(c=>c.type.__TAB__===!0):[],m=!R.length,P=s==="card",B=s==="segment",T=!P&&!B&&this.justifyContent;h.value=[];const V=()=>{const c=l("div",{style:this.tabWrapperStyle,class:`${e}-tabs-wrapper`},T?null:l("div",{class:`${e}-tabs-scroll-padding`,style:b==="top"||b==="bottom"?{width:`${this.tabsPadding}px`}:{height:`${this.tabsPadding}px`}}),m?x.map((z,H)=>(h.value.push(z.props.name),oe(l(ie,Object.assign({},z.props,{internalCreatedByPane:!0,internalLeftPadded:H!==0&&(!T||T==="center"||T==="start"||T==="end")}),z.children?{default:z.children.tab}:void 0)))):R.map((z,H)=>(h.value.push(z.props.name),oe(H!==0&&!T?ye(z):z))),!v&&d&&P?me(d,(m?x.length:R.length)!==0):null,T?null:l("div",{class:`${e}-tabs-scroll-padding`,style:{width:`${this.tabsPadding}px`}}));return l("div",{ref:"tabsElRef",class:`${e}-tabs-nav-scroll-content`},P&&d?l(te,{onResize:this.handleTabsResize},{default:()=>c}):c,P?l("div",{class:`${e}-tabs-pad`}):null,P?null:l("div",{ref:"barElRef",class:`${e}-tabs-bar`}))},W=B?"top":b;return l("div",{class:[`${e}-tabs`,this.themeClass,`${e}-tabs--${s}-type`,`${e}-tabs--${w}-size`,T&&`${e}-tabs--flex`,`${e}-tabs--${W}`],style:this.cssVars},l("div",{class:[`${e}-tabs-nav--${s}-type`,`${e}-tabs-nav--${W}`,`${e}-tabs-nav`]},ve(k,c=>c&&l("div",{class:`${e}-tabs-nav__prefix`},c)),B?l(te,{onResize:this.handleSegmentResize},{default:()=>l("div",{class:`${e}-tabs-rail`,ref:"tabsElRef"},l("div",{class:`${e}-tabs-capsule`,ref:"segmentCapsuleElRef"},l("div",{class:`${e}-tabs-wrapper`},l("div",{class:`${e}-tabs-tab`}))),m?x.map((c,z)=>(h.value.push(c.props.name),l(ie,Object.assign({},c.props,{internalCreatedByPane:!0,internalLeftPadded:z!==0}),c.children?{default:c.children.tab}:void 0))):R.map((c,z)=>(h.value.push(c.props.name),z===0?c:ye(c))))}):l(te,{onResize:this.handleNavResize},{default:()=>l("div",{class:`${e}-tabs-nav-scroll-wrapper`,ref:"scrollWrapperElRef"},["top","bottom"].includes(W)?l(kt,{ref:"xScrollInstRef",onScroll:this.handleScroll},{default:V}):l("div",{class:`${e}-tabs-nav-y-scroll`,onScroll:this.handleScroll,ref:"yScrollElRef"},V()))}),v&&d&&P?me(d,!0):null,ve(A,c=>c&&l("div",{class:`${e}-tabs-nav__suffix`},c))),m&&(this.animated&&(W==="top"||W==="bottom")?l("div",{ref:"tabsPaneWrapperRef",style:g,class:[`${e}-tabs-pane-wrapper`,C]},xe(x,this.mergedValue,this.renderedNames,this.onAnimationBeforeLeave,this.onAnimationEnter,this.onAnimationAfterEnter,this.animationDirection)):xe(x,this.mergedValue,this.renderedNames)))}});function xe(e,s,b,v,d,w,h){const S=[];return e.forEach(C=>{const{name:g,displayDirective:f,"display-directive":k}=C.props,A=R=>f===R||k===R,x=s===g;if(C.key!==void 0&&(C.key=g),x||A("show")||A("show:lazy")&&b.has(g)){b.has(g)||b.add(g);const R=!A("if");S.push(R?$t(C,[[Wt,x]]):C)}}),h?l(Tt,{name:`${h}-transition`,onBeforeLeave:v,onEnter:d,onAfterEnter:w},{default:()=>S}):S}function me(e,s){return l(ie,{ref:"addTabInstRef",key:"__addable",name:"__addable",internalCreatedByPane:!0,internalAddable:!0,internalLeftPadded:s,disabled:typeof e=="object"&&e.disabled})}function ye(e){const s=Pt(e);return s.props?s.props.internalLeftPadded=!0:s.props={internalLeftPadded:!0},s}function oe(e){return Array.isArray(e.dynamicProps)?e.dynamicProps.includes("internalLeftPadded")||e.dynamicProps.push("internalLeftPadded"):e.dynamicProps=["internalLeftPadded"],e}export{ie as _,Yt as a};
