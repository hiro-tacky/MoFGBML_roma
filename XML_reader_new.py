import xml.etree.ElementTree as ET
import matplotlib.pyplot as plt
import csv
import numpy as np
import os
import glob

#######  setting  ############################################################
#数値実験基本設定
DontCareID = 99
FuzzyTypeID = {9:"rectangle", 7:"trapezoid", 3:"triangle", 4:"gaussian", DontCareID:"DontCare"}
ROOTFOLDER = os.getcwd()
cmap = plt.get_cmap("tab10")
#その他の基本設定
IMAGE_SAVE_DIR_PATH = "results/image/" #画像保存用ディレクトリ
ONLY_FINAL_GENERATION_FLAG = False

class DATASET_INFO_CLASS:
    def __init__(self):
        self.PATTERN_NUM = 0
        self.ATTRIBUTE_NUM = 0
        self.CLASS_NUM = 0
        
class FIGURE_PARAMETER_CLASS:
    def __init__(self):
        #デフォルト設定
        self.FIGURE_SIZE = (16, 9) #画像のサイズ
        self.savePath = 'results/graph/tmp'
        
        #scatterの基本設定
        self.MARKER_SIZE = 150
        self.MARKER_ALPHA = 1.0
        self.MARKER_COLOR = 'Black'
        self.MARKER_SHAPE = 'o'
        
        #plotの基本設定
        self.ALPHA = 1.0
        self.ALPHA_BETWEEN = 0.3
        self.LINE_COLOR = 'black'
        self.BETWEEN_COLOR = 'blue'
        
        #figureの基本設定
        self.PLOT_TITLE_FLAG = True
        self.PLOT_TICK_FLAG = True
        self.PLOT_LABEL_FLAG = True
        self.PLOT_LEGEND_FLAG = True
        self.PLOT_GRID_FLAG = True
        
        self.TITLE_FONTSIZE = 40 #タイトルのフォントサイズ
        self.LABEL_FONTSIZE = 50 #ラベルのフォントサイズ
        self.TICK_FONTSIZE = 30 #目盛りのフォントサイズ
        self.LEGEND_FONTSIZE = 30 #汎用のフォントサイズ
        self.MARGIN_SIZE = {'left':0.06, 'right':0.94, 'bottom':0.06, 'top':0.92} #余白サイズ
        self.IS_TRANSPARENT = False #背景の透過
        
    def paper(self):
        #figureの基本設定
        self.PLOT_TITLE_FLAG = False
        self.PLOT_TICK_FLAG = True
        self.PLOT_LABEL_FLAG = True
        self.PLOT_LEGEND_FLAG = False
        self.PLOT_GRID_FLAG = True
        
        self.MARKER_SIZE = 200
        self.LABEL_FONTSIZE = 60 #ラベルのフォントサイズ
        self.TICK_FONTSIZE = 45 #目盛りのフォントサイズ
        self.MARGIN_SIZE = {'left':0.15, 'right':0.98, 'bottom':0.17, 'top':0.98} #余白サイズ
        
    def slide(self):
        #figureの基本設定
        self.PLOT_TITLE_FLAG = False
        self.PLOT_TICK_FLAG = True
        self.PLOT_LABEL_FLAG = True
        self.PLOT_LEGEND_FLAG = True
        self.PLOT_GRID_FLAG = True
        
        self.MARGIN_SIZE = {'left':0.13, 'right':0.98, 'bottom':0.13, 'top':0.98} #余白サイズ


FIGURE_PARAMETER = FIGURE_PARAMETER_CLASS()
FIGURE_PARAMETER.slide()

class RULE_BOOK_CLASS:
    def __init__(self):
        self.IS_DTRA = False
        self.IS_RULENUM_MORE_TAHN_2 = True
        self.COVER_ALL_CLASS = False
        self.TRIAL_NUM_MORE_THAN_HALF = False
        
RULE_BOOK = RULE_BOOK_CLASS()

##############################################################################

### READING XML FILE SUPER CLASS #############################################
class XML:
    """xmlファイルを読み込むためのスーパークラス"""
    def __init__(self, filename, show = True):
        self.tree = ET.parse(filename)
        self.rootNode = self.tree.getroot()
        if show : self.nodelist(self.rootNode)
        self.CurrentElement = [] #木構造の現在参照しているノードの位置を保存する

    def nodelist(self, root):
        c = True
        for i, child in enumerate(root):
            if i<5 or i>len(list(root))-3:
                txt = "node" if child.text == None else str(child.text)
                print("{:3d}: {:20}{:10}{}".format(i, child.tag, str(txt), child.attrib))
            else:
                if c:
                    print("         .\n         .\n         .\n")
                    c = False
        
    def down(self, id):
        """指定したIDの子要素を参照する"""
        self.CurrentElement.append(id)
        buf = self.rootNode
        for i in self.CurrentElement:
            if not list(buf):
                print("NULL")
                self.up()
                return
            buf = buf[i]
        print("current node:" + buf.tag)
        print(buf.attrib)
        print("\n")
        self.nodelist(buf)
            
    def up(self):
        """指定したIDの親要素を参照する"""
        self.CurrentElement.pop(-1)
        buf = self.rootNode
        for i in self.CurrentElement:
            buf = buf[i]
        print("current node:" + buf.tag)
        print(buf.attrib)
        print("\n")
        self.nodelist(buf)
            
    def root(self):
        """木の根にもどる"""
        self.CurrentElement.clear()
        self.nodelist(self.rootNode)
        
    def showAll(self):
        buf = self.rootNode
        for i in self.CurrentElement:
            buf = buf[i]
        for i, child in enumerate(buf):
            txt = "node" if child.text == None else str(child.text)
            print("{:3d}: {:20}{:10}{}".format(i, child.tag, str(txt), child.attrib))
            
    def show(self):
        buf = self.rootNode
        for i in self.CurrentElement:
            buf = buf[i]
        for i, child in enumerate(buf):
            if i<5 or i>len(list(buf))-3:
                txt = "node" if child.text == None else str(child.text)
                print("{:3d}: {:20}{:10}{}".format(i, child.tag, str(txt), child.attrib))
            elif i>9 and i<13:
                print("         .")
###############################################################################                   
                

### FIGURE OBJECT FUNCTIONS ###################################################       
# SETTING SINGLE FIGURE OBJECT
def singleFig_set(FIGURE_PARAMETER, title):
    """保存する画像(グラフ1つ)の基本設定
    入力:ファイル名
    返り値:figureオブジェクト"""
    fig = plt.figure(figsize = FIGURE_PARAMETER.FIGURE_SIZE)
    plt.rcParams["font.family"] = "MS Gothic"
    fig.subplots_adjust(left=FIGURE_PARAMETER.MARGIN_SIZE['left'], right=FIGURE_PARAMETER.MARGIN_SIZE['right'], \
                        bottom=FIGURE_PARAMETER.MARGIN_SIZE['bottom'], top=FIGURE_PARAMETER.MARGIN_SIZE['top'])
    fig.add_subplot(1, 1, 1)
    if title is not None and FIGURE_PARAMETER.PLOT_TITLE_FLAG: fig.suptitle(title, size = FIGURE_PARAMETER.TITLE_FONTSIZE)        
    return fig

# SETTING MULTI FIGURE OBJECT    
def multiFig_set(x_FigNum, y_FigNum, FIGURE_PARAMETER, title):
    """保存する画像(グラフ複数)の基本設定
    入力:ファイル名 axNum:グラフの数
    返り値:figureオブジェクト"""
    fig, axes = plt.subplots(nrows=y_FigNum, ncols=x_FigNum, sharex=False, figsize=(x_FigNum*FIGURE_PARAMETER.FIGURE_SIZE[0], y_FigNum*FIGURE_PARAMETER.FIGURE_SIZE[1]))
    plt.rcParams["font.family"] = "MS Gothic"
    fig.subplots_adjust(left=FIGURE_PARAMETER.MARGIN_SIZE['left'], right=FIGURE_PARAMETER.MARGIN_SIZE['right'], \
                        bottom=FIGURE_PARAMETER.MARGIN_SIZE['bottom'], top=FIGURE_PARAMETER.MARGIN_SIZE['top'])
    fig.suptitle(title, size = FIGURE_PARAMETER.TITLE_FONTSIZE*x_FigNum/2)
    return fig, axes
    
# OUTPUT FIGURE OBJECT
def saveFig(fig, filePath, filename, FIGURE_PARAMETER):
    print(filePath)
    """画像を保存する
    入力:figureオブジェクト, ファイル名, データセット名"""
    os.makedirs(filePath, exist_ok=True)
    plt.rcParams['pdf.fonttype'] = 42
    fig.savefig(filePath + "/" + filename + ".png", transparent=FIGURE_PARAMETER.IS_TRANSPARENT) 
    fig.savefig(filePath + "/" + filename + ".pdf", transparent=FIGURE_PARAMETER.IS_TRANSPARENT) 
    
def plotRuleSetting(ax, FIGURE_PARAMETER):
    ax.set_xlim([-0.05, 1.05])
    ax.set_ylim([-0.05, 1.05])
    if FIGURE_PARAMETER.PLOT_TICK_FLAG:
        ax.tick_params(axis="x", labelsize=FIGURE_PARAMETER.TICK_FONTSIZE)
        ax.tick_params(axis="y", labelsize=FIGURE_PARAMETER.TICK_FONTSIZE)
    else:
        ax.tick_params(labelbottom=False, labelleft=False)
        
    if FIGURE_PARAMETER.PLOT_LABEL_FLAG:
        ax.set_xlabel("属性値", fontsize=FIGURE_PARAMETER.LABEL_FONTSIZE,  fontname="MS Gothic")
        ax.set_ylabel("メンバシップ値", fontsize=FIGURE_PARAMETER.LABEL_FONTSIZE,  fontname="MS Gothic")
        
    if FIGURE_PARAMETER.PLOT_GRID_FLAG:
        ax.grid(True)
        
    if FIGURE_PARAMETER.PLOT_LEGEND_FLAG:
        ax.legend(fontsize=FIGURE_PARAMETER.LEGEND_FONTSIZE).get_frame().set_alpha(1.0)
        
def plotResultSetting(ax, FIGURE_PARAMETER):
    ylim = ax.get_ylim()
    xlim = ax.get_xlim()
    e = 0.2
    ax.set_ylim(ylim[0]*(1+e) - ylim[1]*e, ylim[1]*(1+e) - ylim[0]*e)
    ax.set_xlim(xlim[0], xlim[1]*(1+e) - xlim[0]*e)
    
    if FIGURE_PARAMETER.PLOT_TICK_FLAG:
        ax.tick_params(axis="x", labelsize=FIGURE_PARAMETER.TICK_FONTSIZE)
        ax.tick_params(axis="y", labelsize=FIGURE_PARAMETER.TICK_FONTSIZE)
    else:
        ax.tick_params(labelbottom=False, labelleft=False)
        
    if FIGURE_PARAMETER.PLOT_LABEL_FLAG:
        ax.set_xlabel("ルール数", fontsize=FIGURE_PARAMETER.LABEL_FONTSIZE,  fontname="MS Gothic")
        ax.set_ylabel("誤識別率[%]", fontsize=FIGURE_PARAMETER.LABEL_FONTSIZE,  fontname="MS Gothic")
        
    if FIGURE_PARAMETER.PLOT_LEGEND_FLAG:
        ax.legend(fontsize=FIGURE_PARAMETER.LEGEND_FONTSIZE).get_frame().set_alpha(1.0)
        
    if FIGURE_PARAMETER.PLOT_GRID_FLAG:
        ax.grid(True)
##############################################################################
    
class Pattern:
    def __init__(self, attributeList, classLabel):
        self.attributeList = attributeList
        self.classLabel = classLabel
    
    def toStr(self):
        buf = "Attribute: "
        for dim_i, attr_i in enumerate(self.attributeList):
            buf += "dim{}: {:5f}... ".format(dim_i, attr_i)
        buf += "class:{} ".format(self.classLabel)
        return buf

class Dat:
    def __init__(self, dataName, i, j, ROOTFOLDER=ROOTFOLDER):
        train_data = open(ROOTFOLDER + "/dataset/" + dataName + "/a{}_{}_{}-10tra.dat".format(i,j, dataName), 'r')
        test_data = open(ROOTFOLDER + "/dataset/" + dataName + "/a{}_{}_{}-10tst.dat".format(i,j, dataName), 'r')
        
        self.dtra = []
        for i, rows in enumerate(train_data):
            row = rows.rstrip('\n').split(',')[:-1]
            attributeList = row[:-1]
            attributeList = [float(i) for i in attributeList]
            classLabel = int(row[-1])
            self.dtra.append(Pattern(attributeList, classLabel))
        self.dtst = []
        for i, rows in enumerate(test_data):
            row = rows.rstrip('\n').split(',')[:-1]
            attributeList = row[:-1]
            attributeList = [float(i) for i in attributeList]
            classLabel = int(row[-1])
            self.dtst.append(Pattern(attributeList, classLabel))
            
        self.dtra.pop(0)
        self.dtst.pop(0)
    
        # ファイルを閉じる
        train_data.close()
        test_data.close()
        

### Consts ###################################################################  
class Consts:
    """Costs.propertyのためのクラス"""
    def __init__(self, consts):
        self.settingParameters = {}
        for child in consts:
            if child.text != None:
                self.settingParameters[child.tag] = child.text
            else:
                self.settingParameters[child.tag] = [int(grandChild.text) for grandChild in child]
                
    def getParameter(self, parameterName):
        return self.settingParameters[parameterName]
    
    
### Fuzzy Term ###############################################################            
class FuzzyTerm:
    """Fuzzy Termのためのクラス"""
    def __init__(self, fuzzyTerm):
        self.fuzzyTermID = int(fuzzyTerm.find("fuzzyTermID").text)
        self.fuzzyTermName = fuzzyTerm.find("fuzzyTermName").text
        self.ShapeTypeID = int(fuzzyTerm.find("ShapeTypeID").text)
        self.rectangularShape = fuzzyTerm.find("ShapeTypeName").text
        self.divisionType = fuzzyTerm.find("divisionType").text
        self.partitionNum = int(fuzzyTerm.find("partitionNum").text)
        self.partition_i = int(fuzzyTerm.find("partition_i").text)
        self.parameters = {int(parameters_i.get('id')) : float(parameters_i.text) for parameters_i in fuzzyTerm.find('parameterSet')}
    
    def setAx(self, ax, alpha = 1.0, alpha_between = 0.3, color = "black", color_between = "blue", input_x=None):
        """Ax にメンバシップ関数をプロットする"""
        color_buf = "C{}".format(color) if type(color) is int else color
        
        print(ax)
        ax.set_ylim(-0.05, 1.05)
        if not input_x == None:
            y_data = self.membershipValue(input_x)
            ax.vlines(x=input_x, ymin=0, ymax=y_data, colors="black")
            ax.hlines(y=y_data, xmin=0, xmax=input_x, colors="black")
            
        if self.ShapeTypeID == 3:
            x = np.array([0, self.parameters[0], self.parameters[1], self.parameters[2], 1])
            y = np.array([0, 0, 1, 0, 0])
            ax.plot(x, y, alpha = alpha, color = color_buf)
            y_bottom = [0, 0, 0, 0, 0]
            ax.fill_between(x, y, y_bottom, facecolor=color_between, alpha = alpha_between)
        if self.ShapeTypeID == 4:
            mu = self.parameters[0]
            sigma = self.parameters[1]
            x = np.arange(0, 1, 0.01)
            y = 1 * np.exp(-(x - mu)**2 / (2*sigma**2))
            ax.plot(x, y, alpha = alpha, color = color_buf)
            y_bottom = [0] * 100
            ax.fill_between(x, y, y_bottom, facecolor = color_between, alpha = alpha_between)
        if self.ShapeTypeID == 7:
            x = np.array([0, self.parameters[0], self.parameters[1], self.parameters[2], self.parameters[3], 1])
            y = np.array([0, 0, 1, 1, 0, 0])
            ax.plot(x, y, alpha = alpha, color = color_buf)
            y_bottom = [0, 0, 0, 0, 0, 0]
            ax.fill_between(x, y, y_bottom, facecolor = color_between, alpha = alpha_between)
        if self.ShapeTypeID == 9:
            if self.parameters[0] < 0: x = np.array([0, 0, 0, self.parameters[1], self.parameters[1], 1])
            else: x = np.array([0, self.parameters[0], self.parameters[0], self.parameters[1], self.parameters[1], 1])
            y = np.array([0, 0, 1, 1, 0, 0])
            ax.plot(x, y, alpha = alpha, color = color_buf)
            y_bottom = [0, 0, 0, 0, 0, 0]
            ax.fill_between(x, y, y_bottom, facecolor = color_between, alpha = alpha_between)
        if self.ShapeTypeID == 99:
            x = np.array([0, 0, 1, 1])
            y = np.array([0, 1, 1, 0])
            ax.plot(x, y, alpha = alpha, color = color_buf)
            y_bottom = [0, 0, 0, 0]
            ax.fill_between(x, y, y_bottom, facecolor = color_between, alpha = alpha_between)
            
    def membershipValue(self, input_x):
        """メンバシップ値を出力する"""
        
        if self.ShapeTypeID == 3:
            x_1, x_2, x_3 = self.parameters[0], self.parameters[1], self.parameters[2]
            if input_x <= x_1: return 0
            elif x_1 < input_x and input_x <= x_2: return (input_x - x_1)/(x_2 - x_1)
            elif x_2 < input_x and input_x <= x_3: return (x_3 - input_x)/(x_3 - x_2)
            elif x_3 < input_x: return 0
        if self.ShapeTypeID == 4:
            mu = self.parameters[0]
            sigma = self.parameters[1]
            return  1 * np.exp(-(input_x - mu)**2 / (2*sigma**2))
        if self.ShapeTypeID == 7:
            x_1, x_2, x_3, x_4 = self.parameters[0], self.parameters[1], self.parameters[2], self.parameters[3]
            if input_x <= x_1: return 0
            elif x_1 < input_x and input_x <= x_2: return (input_x - x_1)/(x_2 - x_1)
            elif x_2 < input_x and input_x <= x_3: return 1
            elif x_3 < input_x and input_x <= x_4: return (input_x - x_3)/(x_4 - x_3)
            elif x_4 < input_x: return 0
        if self.ShapeTypeID == 9:
            x_1, x_2 = self.parameters[0], self.parameters[1]
            if input_x <= x_1: return 0
            elif x_1 < input_x and input_x <= x_2: return 1
            elif x_2 < input_x: return 0
        if self.ShapeTypeID == 99:
            return 1
        
        
### Knowledge Base ###########################################################          
class KnowledgeBase:
    """Knowledge bas用のクラス"""
    def __init__(self, knowledge):
        self.fuzzySets = {}
        for fuzzySets in knowledge.findall('fuzzySets'):
            self.fuzzySets[int(fuzzySets.get("dimension"))] = {int(fuzzyTerm.find("fuzzyTermID").text) : FuzzyTerm(fuzzyTerm) for fuzzyTerm in fuzzySets.findall('fuzzyTerm')}
            
    def getFuzzyTerm(self, dimentionID, FuzzyTermID):
        return self.fuzzySets[dimentionID][FuzzyTermID]
    
    def membershipValue(self, dimentionID, FuzzyTermID, input_x):
        return self.fuzzySets[dimentionID][FuzzyTermID].membershipValue(input_x)
        
### MichiganSolution #########################################################
class MichiganSolution():
    def __init__(self, michiganSolution):
        
        self.id = michiganSolution.get('id')
        #antecedent
        self.fuzzyTermIDVector = {int(fuzzySet.get('dimension')) : int(fuzzySet.text) for fuzzySet in michiganSolution.find('fuzzySetList').findall('fuzzySetID')}
        
        #consequent
        self.consequentClass = int(michiganSolution.find('rule').find('consequent').find('classLabel').text) 
        self.ruleWeight = float(michiganSolution.find('rule').find('consequent').find('ruleWeight').text) 
        
    def getFuzzyTermIDList(self):
        return list(self.fuzzyTermIDVector.values())
    
    def getFuzzyTermID(self, dimID):
        return self.fuzzyTermIDVector[dimID]

### PittsburghSolution #######################################################
class PittsburghSolution():
    """pittsbugh型用個体"""
    def __init__(self, pittsburghSolution, consts):
        self.michiganSolutions = {int(michiganSolution.get('id')) : MichiganSolution(michiganSolution) for michiganSolution in pittsburghSolution.findall('michiganSolution')}
        self.objectives = {objective.get('objectiveName') : float(objective.text) for objective in pittsburghSolution.find('objectives').findall('objective')}
        self.consts = consts
        
    def getMichiganSolution(self, michiganSolutionID):
        return self.michiganSolutions[michiganSolutionID]
    
    def getFuzzyTermID(self, michiganSolutionID, dimID):
        return self.michiganSolutions[michiganSolutionID].getFuzzyTermID(dimID)
    
    def getObjective(self, objectiveName):
        return self.objectives[objectiveName]
    
    def isCoverAllClasses(self, consts):
        if len(self.michiganSolutions) < int(consts.getParameter('CLASS_LABEL_NUMBER')):
            return False
        else :
            buf = set()
            for michiganSolution in self.michiganSolutions.values():
                if not michiganSolution.consequentClass in buf : buf.add(michiganSolution.consequentClass)
            if len(buf) == int(consts.getParameter('CLASS_LABEL_NUMBER')): return True
            else: return False
    
    def plotRule(self, knowledge, FIGURE_PARAMETER, michiganSolutionsList=None, coloring='class', data = None):
        if michiganSolutionsList is None: michiganSolutionsList = range(len(self.michiganSolutions))
        if type(michiganSolutionsList) is int: michiganSolutionsList = [michiganSolutionsList]
        
        """１つの画像に全てのif-thenルールをプロット"""
        fig, axes = multiFig_set(y_FigNum=len(self.michiganSolutions), x_FigNum=int(self.consts.getParameter('ATTRIBUTE_NUMBER')), FIGURE_PARAMETER=FIGURE_PARAMETER, title='PittsburghSolution')
        for rule_i in michiganSolutionsList:
            for dim_i, ax in enumerate(fig.axes):
                print(dim_i)
                
                plotRuleSetting(ax, FIGURE_PARAMETER)
                
                if coloring == 'each rules': color_between = cmap(rule_i)
                elif coloring == 'class': color_between = cmap(self.getMichiganSolution(rule_i).consequentClass)
                elif coloring == 'single': color_between = cmap(0)
                
                FuzzyTermID = self.getFuzzyTermID(rule_i, dim_i)
                
                if data is not None:
                    knowledge.getFuzzyTerm(dim_i, FuzzyTermID).setAx(ax, color_between=color_between, input_x=data[dim_i])
                else:
                    knowledge.getFuzzyTerm(dim_i, FuzzyTermID).setAx(ax, color_between=color_between)
        saveFig(fig, FIGURE_PARAMETER.savePath, 'PittsburghSolution', FIGURE_PARAMETER)
        plt.close()
        
    def clacFitness(self, knowledge, data):
        """誤識別率を出力"""
        buf = [0]*len(self.michiganSolutions)
        for michiganSolutionID, michiganSolution in self.michiganSolutions.items():
            tmp = 1
            for dimID, FuzzyTermID in enumerate(michiganSolution.getFuzzyTermIDList()):
                tmp2 = knowledge.membershipValue(dimID, FuzzyTermID, data[dimID])
                tmp *= tmp2
                name = knowledge.getFuzzyTerm(dimID, FuzzyTermID).fuzzyTermName
                print( 'x_{} is {} = {:3f}'.format(dimID, name, tmp2), end=' ')
            tmp *= michiganSolution.ruleWeight
            buf[michiganSolutionID] = (tmp, michiganSolution.consequentClass)
            print('then (Class{}, ruleWeight:{:5f}, Fitness:{:5f})'.format(michiganSolution.consequentClass, michiganSolution.ruleWeight, tmp))
        print()
        return buf    
    
    def clacConclusionClass(self, knowledge, data):
        canClassify = False
        fitness = self.clacFitness(knowledge, data)
        buf_fitness = -1
        buf_class = -1
        for tmp in fitness:
            if buf_fitness < tmp[0]:
                buf_fitness = tmp[0]
                buf_class = tmp[1]
                canClassify = True
            elif buf_fitness == tmp[0]:
                if buf_class != tmp[1]:
                    canClassify = False
        if canClassify and buf_fitness >= 0:
            return buf_class
        else :
            return -1
    
    def calcConclusionClassJudge(self, knowledge, pattern):
        classBuf = self.clacConclusionClass(knowledge, pattern.attributeList)
        return classBuf == pattern.classLabel
    
    def calcErrorRate(self, knowledge, Dat, isDtra):
        cnt = 0
        if isDtra:
            for pattern_i in Dat.dtra:
                print(pattern_i.toStr())
                if self.calcConclusionClassJudge(knowledge, pattern_i): cnt += 1
            return 1-float(cnt)/len(Dat.dtra)
        else:
            for pattern_i in Dat.dtst:
                print(pattern_i.toStr())
                if self.calcConclusionClassJudge(knowledge, pattern_i): cnt += 1
            return 1-float(cnt)/len(Dat.dtst)
    
        
### Population ###############################################################
class Population():
    """個体群"""
    def __init__(self, population, consts):
        self.consts = consts
        self.pittsburghSolutions = {id_: PittsburghSolution(pittsburghSolution, consts) for id_, pittsburghSolution in enumerate(population.findall('pittsburghSolution'))}
    
    def getPittsburghSolution(self, pittsburghSolutionID):
        return self.pittsburghSolutions[pittsburghSolutionID]
    
    def getMichiganSolution(self, pittsburghSolutionID, MichiganSolutionID):
        return self.pittsburghSolutions[pittsburghSolutionID].michiganSolutions[MichiganSolutionID]
    
    def getPittsburghObjectives_All(self, RULE_BOOK):
        """全個体をlist型で出力"""
        dataList = []
        for pittsburghSolution_i in self.pittsburghSolutions.values():
            if not RULE_BOOK.IS_RULENUM_MORE_TAHN_2 or pittsburghSolution_i.getObjective('NumberOfRule') > 1:
                if not RULE_BOOK.COVER_ALL_CLASS or pittsburghSolution_i.isCoverAllClasses(self.consts):
                    if RULE_BOOK.IS_DTRA: dataList.append((pittsburghSolution_i.getObjective('NumberOfRule'), pittsburghSolution_i.getObjective('ErrorRateDtra')))
                    else : dataList.append((pittsburghSolution_i.getObjective('NumberOfRule'), pittsburghSolution_i.getObjective('ErrorRateDtst')))
        return dataList
    
    def getPittsburghObjectives_Average(self, RULE_BOOK):
        """各個体の平均値をlist型で出力"""
        dataBuf = {}
        dataList = []
        for pittsburghSolution_i in self.pittsburghSolutions.values():
            if not RULE_BOOK.IS_RULENUM_MORE_TAHN_2 or pittsburghSolution_i.getObjective('NumberOfRule') > 1:
                if not RULE_BOOK.COVER_ALL_CLASS or pittsburghSolution_i.isCoverAllClasses(self.consts):
                    if pittsburghSolution_i.getObjective('NumberOfRule') in dataBuf:
                        if RULE_BOOK.IS_DTRA: dataBuf[pittsburghSolution_i.getObjective('NumberOfRule')]['sum'] += pittsburghSolution_i.getObjective('ErrorRateDtra')
                        else : dataBuf[pittsburghSolution_i.getObjective('NumberOfRule')]['sum'] += pittsburghSolution_i.getObjective('ErrorRateDtst')
                        dataBuf[pittsburghSolution_i.getObjective('NumberOfRule')]['num'] += 1
                    else:
                        if RULE_BOOK.IS_DTRA: dataBuf[pittsburghSolution_i.getObjective('NumberOfRule')] = {'sum':pittsburghSolution_i.getObjective('ErrorRateDtra'), 'num':1}
                        else : dataBuf[pittsburghSolution_i.getObjective('NumberOfRule')] = {'sum':pittsburghSolution_i.getObjective('ErrorRateDtst'), 'num':1}
        dataList = [(key, value['sum']/value['num']) for key, value in dataBuf.items()]
        return dataList

### Generations ##############################################################

class Generations():
    def __init__(self, generations, consts):
        self.consts = consts
        self.evaluationID = int(generations.get('evaluation'))
        self.populations = Population(generations.find('population'), consts)
        self.knowledge = KnowledgeBase(generations.find('knowledgeBase'))
    
    def getKnowledge(self):
        return self.knowledge
    
    def getConsts(self):
        return self.consts
    
    def getPopulation(self):
        return self.populations
    
    def getPittsburghSolution(self, pittsburghSolutionID):
        return self.populations.getPittsburghSolution(pittsburghSolutionID)
    
    def plotRule(self, FIGURE_PARAMETER, pittsburghSolutionID):
        self.populations.getPittsburghSolution(pittsburghSolutionID).plotRule(self.knowledge, FIGURE_PARAMETER)
    
    def calcErrorRate(self, pittsburghSolutionID, Dat, isDtra):
        return self.populations.getPittsburghSolution(pittsburghSolutionID).calcErrorRate(self.knowledge, Dat, isDtra)
    
    def usedFuzzySetsCount(self):
        usedFuzzySet = []
        for fuzzySets_dimension in self.getKnowledge().fuzzySets.values():
            usedFuzzySet.append({fuzzyTerm:0 for fuzzyTerm in fuzzySets_dimension.values()})
            
        for pittsburghSolution_i in self.getPopulation().pittsburghSolutions.values():
            for michiganSolution_i in pittsburghSolution_i.michiganSolutions.values():
                for dimension, fuzzyTermID in michiganSolution_i.fuzzyTermIDVector.items():
                    usedFuzzySet[dimension][self.getKnowledge().getFuzzyTerm(dimension, fuzzyTermID)] += 1
        return usedFuzzySet
    
### Trial Manager ############################################################

class TrialManager(XML):
    def __init__(self, path, savePath):
        super().__init__(path, show = False)
        print(path)
        """一試行用のクラス
        入力: path=xmlファイルのパス, savePath 保存先指定フォルダ"""
        self.savePath = savePath
        self.consts = Consts(self.rootNode.find('consts'))
        if ONLY_FINAL_GENERATION_FLAG:
            self.generations = {int(generations.get('evaluation')) : Generations(generations, self.consts) for generations in self.rootNode.findall('generations')\
                                if generations.get('evaluation') == self.consts.getParameter('TERMINATE_EVALUATION')}
        else:
            self.generations = {int(generations.get('evaluation')) : Generations(generations, self.consts) for generations in self.rootNode.findall('generations')}
            
    def getGeneration(self, evaluation=None):
        if evaluation is None : evaluation = int(self.consts.getParameter('TERMINATE_EVALUATION'))
        return self.generations[evaluation]
        
    def getPopulation(self, evaluation = None):
        if evaluation is None : evaluation = int(self.consts.getParameter('TERMINATE_EVALUATION'))
        return self.generations[evaluation].getPopulation()
        
    def getPittsburghSolution(self, evaluation, pittsburghSolutionID):
        if evaluation is None : evaluation = int(self.consts.getParameter('TERMINATE_EVALUATION'))
        return self.populations[evaluation].getPopulation().getPittsburghSolution(pittsburghSolutionID)
    
    def getConsts(self):
        return self.consts

### Experiment Manager #######################################################
class ExperimentManager():
    def __init__(self, path, savePath, label, dataName, algorithmID, experimentID):
        """一つの実験用のクラス
        入力: path=xmlファイルのフォルダのパス, savePath 保存先指定フォルダ"""
        self.label = label
        self.dataName = dataName
        self.algorithmID = algorithmID
        self.experimentID = experimentID
        
        self.savePath = savePath
        self.TrialManagers = {}
        XMLpaths = [str.replace(os.path.sep, "/") for str in glob.glob(path + "/results/" + algorithmID + "/" + dataName + "/" + experimentID + "*/*.xml")]
        self.savePath = "results/graph/" + algorithmID + "/" + dataName + "/"
        for id_, XMLpath in enumerate(XMLpaths):
            experimentName = "{:s}{:0>2}".format(experimentID, id_)
            self.TrialManagers[experimentName] = TrialManager(XMLpath, self.savePath + experimentName)
        print(len(self.TrialManagers))
        
    def getTrialManager(self, trialID):
        return self.TrialManagers["{:s}{:0>2}".format(self.experimentID, trialID)]
    
    def getGeneration(self, trialID, evaluation):
        if evaluation is None : evaluation = int(self.TrialManagers['trial{:0>2}'.format(trialID)].consts.getParameter('TERMINATE_EVALUATION'))
        return self.TrialManagers["{:s}{:0>2}".format(self.experimentID, trialID)].getGeneration(evaluation)
    
    def getPopulation(self, trialID, evaluation):
        if evaluation is None : evaluation = int(self.TrialManagers['trial{:0>2}'.format(trialID)].consts.getParameter('TERMINATE_EVALUATION'))
        return self.TrialManagers["{:s}{:0>2}".format(self.experimentID, trialID)].getGeneration(evaluation).getPopulation()
    
    def getPittsburghSolution(self, trialID, evaluation, pittsburghSolutionID):
        if evaluation is None : evaluation = int(self.TrialManagers['trial{:0>2}'.format(trialID)].consts.getParameter('TERMINATE_EVALUATION'))
        return self.TrialManagers["{:s}{:0>2}".format(self.experimentID, trialID)].getGeneration(evaluation).getPopulation().getPittsburghSolution(pittsburghSolutionID)
    
    def getPittsburghObjectives_Average(self, evaluation, RULE_BOOK):
        """全試行で得られた全個体の平均値を出力"""
        michiganSolutionsBuf = {}
        michiganSolutions = []
        for trial in self.TrialManagers.values():
            tmp = trial.getPopulation(evaluation).getPittsburghObjectives_Average(RULE_BOOK)
            for data in tmp:
                if data[0] in michiganSolutionsBuf:
                    michiganSolutionsBuf[data[0]]['sum'] += data[1]
                    michiganSolutionsBuf[data[0]]['num'] += 1
                else:
                    michiganSolutionsBuf[data[0]] = {'sum':data[1], 'num':1}
                    
        if RULE_BOOK.TRIAL_NUM_MORE_THAN_HALF:
            michiganSolutions = [(key, value['sum']/value['num']) for key, value in michiganSolutionsBuf.items() if value['num'] > len(self.TrialManagers)/2]
        else:
            michiganSolutions = [(key, value['sum']/value['num']) for key, value in michiganSolutionsBuf.items()]
            
        return michiganSolutions
    
    def getPittsburghObjectives_Alltrials(self, evaluation, RULE_BOOK):
        """1回試行で得られた個体群の平均値を出力"""
        michiganSolutions = []
        for trial in self.TrialManagers.values():
            buf = trial.getPopulation(evaluation).getPittsburghObjectives_Average(RULE_BOOK)
            michiganSolutions.extend(buf)
        return michiganSolutions

    def getPittsburghObjectives_Allsolutions(self, evaluation, RULE_BOOK):
        """全個体を出力"""
        michiganSolutions = []
        for trial in self.TrialManagers.values():
            population = trial.getPopulation(evaluation)
            buf = population.getPittsburghObjectives_All(RULE_BOOK)
            michiganSolutions.extend(buf)
        return michiganSolutions        
    
##############################################################################


### PLOT XML FILE ############################################################

class Master:
    def __init__(self, dataName, algorithmIDList, experimentLabelList, experimentID, ROOTFOLDER = ROOTFOLDER):
        self.dataName = dataName
        self.ROOTFOLDER = ROOTFOLDER
        self.DATA_LABEL = experimentLabelList
        self.experimentID = experimentID
        self.master = {}
        for index, experimentLabel_i in enumerate(experimentLabelList):
            self.master[experimentLabel_i] = ExperimentManager(self.ROOTFOLDER, self.ROOTFOLDER, experimentLabel_i, self.dataName, algorithmIDList[index], experimentID)
            
    def getExperimentManager(self, dataLabel):
        return self.master[dataLabel]
    
    def getTrialManager(self, dataLabel, trialID):
        return self.master[dataLabel].TrialManagers["{:s}{:0>2}".format(self.experimentID, trialID)]
    
    def getGeneration(self, dataLabel, trialID, evaluation):
        if evaluation is None : evaluation = int(self.master[dataLabel].TrialManagers['trial{:0>2}'.format(trialID)].consts.getParameter('TERMINATE_EVALUATION'))
        return self.master[dataLabel].TrialManagers["{:s}{:0>2}".format(self.experimentID, trialID)].getGeneration(evaluation)
    
    def getPopulation(self, dataLabel, trialID, evaluation):
        if evaluation is None : evaluation = int(self.master[dataLabel].TrialManagers['trial{:0>2}'.format(trialID)].consts.getParameter('TERMINATE_EVALUATION'))
        return self.master[dataLabel].TrialManagers["{:s}{:0>2}".format(self.experimentID, trialID)].getGeneration(evaluation).getPopulation()
    
    def getPittsburghSolution(self, dataLabel, trialID, evaluation, pittsburghSolutionID):
        if evaluation is None : evaluation = int(self.master[dataLabel].TrialManagers['trial{:0>2}'.format(trialID)].consts.getParameter('TERMINATE_EVALUATION'))
        return self.master[dataLabel].TrialManagers["{:s}{:0>2}".format(self.experimentID, trialID)].getPopulation(evaluation).getPittsburghSolution(pittsburghSolutionID)
    
    def plotResult(self, evaluation, mode, RULE_BOOK, FIGURE_PARAMETER, experimentLabelList, saveFigFlag=False):
        """学習結果をプロット．
        model: ExperimentManager識別用, evaluation: 世代数, mode: プロットするモード選択, xlim: x軸のレンジ, ylim: y軸のレンジ, 
        alpha: ドットの透明度, ruleBook: 表示個体の制限, plotData: 評価用データor学習用データ, saveFigFlag: 画像を保存するか否か"""
        
        if type(experimentLabelList) is not list: experimentLabelList = [experimentLabelList]
        if mode=='Allsolutions':
            fig = singleFig_set(FIGURE_PARAMETER, 'Allsolutions' + str(evaluation))
            ax = fig.gca()
            for experimentLabel in experimentLabelList:
                experimentManager = self.master[experimentLabel]
                data = experimentManager.getPittsburghObjectives_Allsolutions(evaluation=evaluation, RULE_BOOK=RULE_BOOK)
                ax.scatter([data_i[0] for data_i in data], [data_i[1]*100 for data_i in data], s=FIGURE_PARAMETER.MARKER_SIZE, alpha=FIGURE_PARAMETER.MARKER_ALPHA, label=experimentLabel)

        elif mode=='Alltrials':
            fig = singleFig_set(FIGURE_PARAMETER, 'Alltrials' + str(evaluation))
            ax = fig.gca()        
            for experimentLabel in experimentLabelList:
                experimentManager = self.master[experimentLabel]
                data = experimentManager.getPittsburghObjectives_Alltrials(evaluation=evaluation, RULE_BOOK=RULE_BOOK)
                ax.scatter([data_i[0] for data_i in data], [data_i[1]*100 for data_i in data], s=FIGURE_PARAMETER.MARKER_SIZE, alpha=FIGURE_PARAMETER.MARKER_ALPHA, label=experimentLabel)                
        
        elif mode=='Average':
            fig = singleFig_set(FIGURE_PARAMETER, 'Average' + str(evaluation))
            ax = fig.gca()        
            for experimentLabel in experimentLabelList:
                experimentManager = self.master[experimentLabel]
                data = experimentManager.getPittsburghObjectives_Average(evaluation=evaluation, RULE_BOOK=RULE_BOOK)
                ax.scatter([data_i[0] for data_i in data], [data_i[1]*100 for data_i in data], s=FIGURE_PARAMETER.MARKER_SIZE, alpha=FIGURE_PARAMETER.MARKER_ALPHA, label=experimentLabel)
        
        plotResultSetting(ax, FIGURE_PARAMETER)
        
        if saveFigFlag:
            print(RULE_BOOK.IS_DTRA)
            if RULE_BOOK.IS_DTRA:
                dataType = "Dtra"
            else:
                dataType = "Dtst"
            fileName = "result_{:s}_{:s}_{:08}".format(self.dataName, dataType, evaluation)
            saveFig(fig, self.ROOTFOLDER + "/results/graph/" + self.dataName + "/" + dataType + "/" + mode, fileName, FIGURE_PARAMETER)
        plt.show()
        
    def plotUsedFuzzySets(self, dataLabel, evaluation):
        return_obj = []
        for trial in self.getExperimentManager(dataLabel).TrialManagers.values():
            generation = trial.getGeneration(evaluation)
            return_obj.append(generation.usedFuzzySetsCount())
        return return_obj
    
    def plotPie(self, dataLabel, evaluation, FIGURE_PARAMETER=FIGURE_PARAMETER):
        usedFuzzySets = self.plotUsedFuzzySets(dataLabel, evaluation)
        dim = len(usedFuzzySets[0])
    
        label_1 = ['均等分割区間集合', '不均等分割区間集合', '均等分割三角型条件部集合', '不均等分割三角型条件部集合', '均等分割ガウシアン集合', '不均等分割ガウシアン集合']
        partition = [2, 3, 4, 5]
        k = len(partition)
        
        x_1 = [[0] * len(label_1) for i in range(dim)]
        x_2 = [[0] * (len(label_1) * len(partition)) for i in range(dim)]
        
        colors_1 = [cmap(0), cmap(1), cmap(2), cmap(3), cmap(4), cmap(5)]
        colors_2 = []
        alphas = [1, 0.8, 0.6, 0.4]
        for i in range(len(label_1)):
            for j in range(len(partition)):
                c_r, c_g, c_b, _ = cmap(i)
                colors_2.append((c_r, c_g, c_b, alphas[j])) 
                
        for trial_i, usedFuzzySets_trial in enumerate(usedFuzzySets):
            for dim_i, usedFuzzySets_dim in enumerate(usedFuzzySets_trial):
                for fuzzyTerm, fuzzySetNum in usedFuzzySets_dim.items():
                    if fuzzyTerm.fuzzyTermID == 0:
                        continue
                    if fuzzyTerm.divisionType == 'equalDivision' and fuzzyTerm.ShapeTypeID == 9:
                        x_1[dim_i][0] += fuzzySetNum
                        x_2[dim_i][k*0 + (fuzzyTerm.partitionNum-2)] += fuzzySetNum
                    elif fuzzyTerm.divisionType == 'entropyDivision' and fuzzyTerm.ShapeTypeID == 9:
                        x_1[dim_i][1] += fuzzySetNum
                        x_2[dim_i][k*1 + (fuzzyTerm.partitionNum-2)] += fuzzySetNum
                    elif fuzzyTerm.divisionType == 'equalDivision' and (fuzzyTerm.ShapeTypeID == 3 or fuzzyTerm.ShapeTypeID == 7):
                        x_1[dim_i][2] += fuzzySetNum
                        x_2[dim_i][k*2 + (fuzzyTerm.partitionNum-2)] += fuzzySetNum                
                    elif fuzzyTerm.divisionType == 'entropyDivision' and (fuzzyTerm.ShapeTypeID == 3 or fuzzyTerm.ShapeTypeID == 7):
                        x_1[dim_i][3] += fuzzySetNum
                        x_2[dim_i][k*3 + (fuzzyTerm.partitionNum-2)] += fuzzySetNum           
                    elif fuzzyTerm.divisionType == 'equalDivision' and fuzzyTerm.ShapeTypeID == 4:
                        x_1[dim_i][4] += fuzzySetNum
                        x_2[dim_i][k*4 + (fuzzyTerm.partitionNum-2)] += fuzzySetNum              
                    elif fuzzyTerm.divisionType == 'entropyDivision' and fuzzyTerm.ShapeTypeID == 4:
                        x_1[dim_i][5] += fuzzySetNum
                        x_2[dim_i][k*5 + (fuzzyTerm.partitionNum-2)] += fuzzySetNum
                        
        for dim_i in range(dim):
            label_2 = [2, 3, 4, 5] * len(label_1)
            fig, ax = plt.subplots(figsize = (9, 9))
            fig.subplots_adjust(left=0, right=1, bottom=0,  top=1)
            print(x_1[dim_i], x_2[dim_i])
            for index_, value_ in enumerate(x_2[dim_i]):
                if value_ == 0 : label_2[index_] = ''
            patches, texts = ax.pie(x_2[dim_i], colors=colors_2, labels=label_2, radius=1.2, labeldistance=0.85, startangle=90, wedgeprops={'linewidth': 0.5, 'edgecolor':"black"}, counterclock = False)
            for t in texts:
                t.set_size(35)
            ax.pie(x_1[dim_i], colors=colors_1, radius=0.8, startangle=90, wedgeprops={'linewidth': 0.5, 'edgecolor':"black"}, counterclock = False)
            saveFig(fig, os.getcwd() + "/results/graph/" + self.dataName + "/usedFuzzySetRate/" + dataLabel, "usedFuzzySetRate_" + str(dim_i), FIGURE_PARAMETER)
            saveFig(fig, os.getcwd() + "/results/graph/" + self.dataName + "/usedFuzzySetRate/" + dataLabel, "usedFuzzySetRate_" + str(dim_i), FIGURE_PARAMETER)
            plt.show()
            plt.close('all')
        
    def plotCsv(self, dataLabel, evaluation):
        usedFuzzySets = self.plotUsedFuzzySets(dataLabel, evaluation)
        dim = len(usedFuzzySets[0])
    
        label = ['均等分割区間集合', '不均等分割区間集合', '均等分割三角型条件部集合', '不均等分割三角型条件部集合', '均等分割ガウシアン集合', '不均等分割ガウシアン集合']
        partition_num = [2, 3, 4, 5]
        
        dim_buf = [[]for i in range(dim)]
                
        for trial_i, usedFuzzySets_trial in enumerate(usedFuzzySets):
            for dim_i, usedFuzzySets_dim in enumerate(usedFuzzySets_trial):
                usedFuzzySets_buf = {}
                for label_i in label:
                    tmp = {}
                    for partition_i in partition_num:
                        tmp2 = {}
                        for i in range(partition_i):
                            tmp2[i] = 0
                        tmp[partition_i] = tmp2
                    usedFuzzySets_buf[label_i] = tmp
                        
                for fuzzyTerm, fuzzySetNum in usedFuzzySets_dim.items():
                    if fuzzyTerm.fuzzyTermID == 0:
                        continue
                    if fuzzyTerm.divisionType == 'equalDivision' and fuzzyTerm.ShapeTypeID == 9:
                        usedFuzzySets_buf['均等分割区間集合'][fuzzyTerm.partitionNum][fuzzyTerm.partition_i] += fuzzySetNum
                    elif fuzzyTerm.divisionType == 'entropyDivision' and fuzzyTerm.ShapeTypeID == 9:
                        usedFuzzySets_buf['不均等分割区間集合'][fuzzyTerm.partitionNum][fuzzyTerm.partition_i] += fuzzySetNum
                    elif fuzzyTerm.divisionType == 'equalDivision' and (fuzzyTerm.ShapeTypeID == 3 or fuzzyTerm.ShapeTypeID == 7):
                        usedFuzzySets_buf['均等分割三角型条件部集合'][fuzzyTerm.partitionNum][fuzzyTerm.partition_i] += fuzzySetNum          
                    elif fuzzyTerm.divisionType == 'entropyDivision' and (fuzzyTerm.ShapeTypeID == 3 or fuzzyTerm.ShapeTypeID == 7):
                        usedFuzzySets_buf['不均等分割三角型条件部集合'][fuzzyTerm.partitionNum][fuzzyTerm.partition_i] += fuzzySetNum
                    elif fuzzyTerm.divisionType == 'equalDivision' and fuzzyTerm.ShapeTypeID == 4:
                        usedFuzzySets_buf['均等分割ガウシアン集合'][fuzzyTerm.partitionNum][fuzzyTerm.partition_i] += fuzzySetNum           
                    elif fuzzyTerm.divisionType == 'entropyDivision' and fuzzyTerm.ShapeTypeID == 4:
                        usedFuzzySets_buf['不均等分割ガウシアン集合'][fuzzyTerm.partitionNum][fuzzyTerm.partition_i] += fuzzySetNum
                
                dim_buf[dim_i].append(usedFuzzySets_buf)
        
        for dim_i in range(dim):
            os.makedirs(ROOTFOLDER + "/results/graph/" + self.dataName + "/usedFuzzySetRate/csv/", exist_ok=True)
            f = open(ROOTFOLDER + "/results/graph/" + self.dataName + "/usedFuzzySetRate/csv/out_" + str(dim_i)+'.csv', 'w', newline='')
            writer = csv.writer(f)
            for trial_i, trial in enumerate(dim_buf[dim_i]):
                row = ["trial:{:2d}".format(trial_i)]
                for label_i in trial.values():
                    for partition_num_i in label_i.values():
                        for partition_i in partition_num_i.values():
                            row.append(partition_i)
                writer.writerow(row)
            f.close()
    
        
    def plotCsv2(self, dataLabel, evaluation):
        usedFuzzySets = self.plotUsedFuzzySets(dataLabel, evaluation)
        dim = len(usedFuzzySets[0])
    
        label = ['均等分割区間集合', '不均等分割区間集合', '均等分割三角型条件部集合', '不均等分割三角型条件部集合', '均等分割ガウシアン集合', '不均等分割ガウシアン集合']
        partition_num = [2, 3, 4, 5]
        
        dim_buf = [[]for i in range(dim)]
                
        for trial_i, usedFuzzySets_trial in enumerate(usedFuzzySets):
            for dim_i, usedFuzzySets_dim in enumerate(usedFuzzySets_trial):
                usedFuzzySets_buf = {}
                for label_i in label:
                    for partition_i in partition_num:
                        for i in range(partition_i):
                            usedFuzzySets_buf[label_i + '_' + str(partition_i) + '_' + str(i+1)] = 0
                 
                        
                for fuzzyTerm, fuzzySetNum in usedFuzzySets_dim.items():
                    if fuzzyTerm.fuzzyTermID == 0:
                        continue
                    if fuzzyTerm.divisionType == 'equalDivision' and fuzzyTerm.ShapeTypeID == 9:
                        usedFuzzySets_buf['均等分割区間集合_' + str(fuzzyTerm.partitionNum) + '_' + str(fuzzyTerm.partition_i+1)] += fuzzySetNum
                    elif fuzzyTerm.divisionType == 'entropyDivision' and fuzzyTerm.ShapeTypeID == 9:
                        usedFuzzySets_buf['不均等分割区間集合_' + str(fuzzyTerm.partitionNum) + '_' + str(fuzzyTerm.partition_i+1)] += fuzzySetNum
                    elif fuzzyTerm.divisionType == 'equalDivision' and (fuzzyTerm.ShapeTypeID == 3 or fuzzyTerm.ShapeTypeID == 7):
                        usedFuzzySets_buf['均等分割三角型条件部集合_' + str(fuzzyTerm.partitionNum) + '_' + str(fuzzyTerm.partition_i+1)] += fuzzySetNum          
                    elif fuzzyTerm.divisionType == 'entropyDivision' and (fuzzyTerm.ShapeTypeID == 3 or fuzzyTerm.ShapeTypeID == 7):
                        usedFuzzySets_buf['不均等分割三角型条件部集合_' + str(fuzzyTerm.partitionNum) + '_' + str(fuzzyTerm.partition_i+1)] += fuzzySetNum
                    elif fuzzyTerm.divisionType == 'equalDivision' and fuzzyTerm.ShapeTypeID == 4:
                        usedFuzzySets_buf['均等分割ガウシアン集合_' + str(fuzzyTerm.partitionNum) + '_' + str(fuzzyTerm.partition_i+1)] += fuzzySetNum           
                    elif fuzzyTerm.divisionType == 'entropyDivision' and fuzzyTerm.ShapeTypeID == 4:
                        usedFuzzySets_buf['不均等分割ガウシアン集合_' + str(fuzzyTerm.partitionNum) + '_' + str(fuzzyTerm.partition_i+1)] += fuzzySetNum
                
                dim_buf[dim_i].append(usedFuzzySets_buf)
        
        for dim_i, usedFuzzySets_dim in enumerate(dim_buf):
            print('dim: ' + str(dim_i))
            os.makedirs(ROOTFOLDER + "/results/graph/" + self.dataName + "/usedFuzzySetRate/csv/", exist_ok=True)
            f = open(ROOTFOLDER + "/results/graph/" + self.dataName + "/usedFuzzySetRate/csv/out_" + str(dim_i)+'.csv', 'w', newline='')
            writer = csv.writer(f)
            row = ['']
            for i in range(15):
                row.append(str(i+1))
                row.append('')
            writer.writerow(row)
            for trial_i, usedFuzzySets_trial in enumerate(usedFuzzySets_dim):
                row = ['trial: ' + str(trial_i)]
                usedFuzzySets_sorted = sorted(usedFuzzySets_trial.items(), key = lambda fuzzyTermNum : fuzzyTermNum[1],reverse=True)
                for usedFuzzySets_tmp in usedFuzzySets_sorted[:15]:
                    if usedFuzzySets_tmp[1] != 0:
                        row.append(usedFuzzySets_tmp[0])
                        row.append(str(usedFuzzySets_tmp[1]))
                writer.writerow(row)
            f.close()
###############################################################################
