---
navigation:
  title: "砧音系统导论"
  icon: "anvilcraft_reverberation:clang_crystal"
  position: 0
  parent: anvilcraft_guideme:reverberation.md
item_ids:
  - anvilcraft_reverberation:acoustic_component
  - anvilcraft_reverberation:clang_crystal
  - anvilcraft_reverberation:merge_sound_pillar
  - anvilcraft_reverberation:anvil_sound_reactor
---

# 砧音系统导论

<Row>
<ItemImage id="anvilcraft_reverberation:acoustic_component" scale="3"></ItemImage>
<ItemImage id="anvilcraft_reverberation:clang_crystal" scale="3"></ItemImage>
<ItemImage id="anvilcraft_reverberation:merge_sound_pillar" scale="3"></ItemImage>
<ItemImage id="anvilcraft_reverberation:anvil_sound_reactor" scale="3"></ItemImage>
</Row>

---

# <ItemLink id="anvilcraft_reverberation:clang_crystal" />

<Row>
<Recipe id="anvilcraft_reverberation:acoustic_component"></Recipe>
<Recipe id="anvilcraft_reverberation:clang_crystal"></Recipe>
</Row>

当 <ItemLink id="anvilcraft_reverberation:clang_crystal" /> 被铁砧砸击时，会发出一道*砧音波*，只能传播到y轴相同的位置，最大**切比雪夫距离**为 6 

## 砧音波

砧音波蕴含着的丰富的能量，其与*铁砧砸落高度*成正比

## 切比雪夫距离

计算两个点之间在 x y z 三个轴上的距离，最大值即为*切比雪夫距离*

---

# <ItemLink id="anvilcraft_reverberation:merge_sound_pillar" />

<Recipe id="anvilcraft_reverberation:merge_sound_pillar"></Recipe>

<ItemLink id="anvilcraft_reverberation:merge_sound_pillar" />可以接受来自**切比雪夫距离**为 6 以内的声源发出的*砧音波*

每秒会将过去 1s 内接收到*砧音波*总结为*合音*并存储

## 合音

合音的能量就是所有*砧音波*的总能量，记载了该周期内所有砧音波的信息

---

# <ItemLink id="anvilcraft_reverberation:anvil_sound_reactor" />

<Recipe id="anvilcraft_reverberation:anvil_sound_reactor"></Recipe>

- <ItemLink id="anvilcraft_reverberation:anvil_sound_reactor" />只能放置在<ItemLink id="anvilcraft_reverberation:merge_sound_pillar" />之上使用
- 可以进行*砧音反应*，借助砧音将一个物品转化为另一个物品

--- 

# 砧音反应

*砧音反应*共有四类需求:
1. 能量
2. 声源数量
3. 音色
4. 氛围

前期的配方只会有能量需求

> 利用*砧音反应*制造<ItemLink id="anvilcraft_reverberation:acoustic_component" />，会便宜得多，快去试试吧