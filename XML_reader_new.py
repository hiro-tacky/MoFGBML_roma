import xml.etree.ElementTree as ET
import matplotlib.pyplot as plt
import numpy as np
import os
from datetime import datetime
import glob
from scipy.stats import gaussian_kde
from sklearn import preprocessing
from ipywidgets import interact, IntSlider, Select, FloatSlider

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
        self.MARKER_SIZE = 100
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
        
        self.TITLE_FONTSIZE = 30 #タイトルのフォントサイズ
        self.LABEL_FONTSIZE = 30 #ラベルのフォントサイズ
        self.TICK_FONTSIZE = 20 #目盛りのフォントサイズ
        self.LEGEND_FONTSIZE = 20 #汎用のフォントサイズ
        self.MARGIN_SIZE = {'left':0.06, 'right':0.94, 'bottom':0.06, 'top':0.94} #余白サイズ
        self.IS_TRANSPARENT = False #背景の透過
        
    def simple(self):
        #figureの基本設定
        self.PLOT_TITLE_FLAG = False
        self.PLOT_TICK_FLAG = False
        self.PLOT_LABEL_FLAG = False
        self.PLOT_LEGEND_FLAG = True
        self.PLOT_GRID_FLAG = True

class RULE_BOOK_CLASS:
    def __init__(self):
        self.IS_DTRA = True
        self.IS_RULENUM_MORE_TAHN_2 = True
        self.COVER_ALL_CLASS = True
        self.TRIAL_NUM_MORE_THAN_HALF = True

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
def singleFig_set(figure_parameter, title):
    """保存する画像(グラフ1つ)の基本設定
    入力:ファイル名
    返り値:figureオブジェクト"""
    fig = plt.figure(figsize = figure_parameter.FIGURE_SIZE)
    plt.rcParams["font.family"] = "MS Gothic"
    fig.subplots_adjust(left=figure_parameter.MARGIN_SIZE['left'], right=figure_parameter.MARGIN_SIZE['right'], \
                        bottom=figure_parameter.MARGIN_SIZE['bottom'], top=figure_parameter.MARGIN_SIZE['top'])
    fig.add_subplot(1, 1, 1)
    if title is not None: fig.suptitle(title, size = figure_parameter.TITLE_FONTSIZE)        
    return fig

# SETTING MULTI FIGURE OBJECT    
def multiFig_set(x_FigNum, y_FigNum, figure_parameter, title):
    """保存する画像(グラフ複数)の基本設定
    入力:ファイル名 axNum:グラフの数
    返り値:figureオブジェクト"""
    fig = plt.figure(figsize = (x_FigNum*figure_parameter.FIGURE_SIZE[0], y_FigNum*figure_parameter.FIGURE_SIZE[1]))
    plt.rcParams["font.family"] = "MS Gothic"
    fig.subplots_adjust(left=figure_parameter.MARGIN_SIZE['left'], right=figure_parameter.MARGIN_SIZE['right'], \
                        bottom=figure_parameter.MARGIN_SIZE['bottom'], top=figure_parameter.MARGIN_SIZE['top'])
    fig.suptitle(title, size = figure_parameter.TITLE_FONTSIZE*x_FigNum/2)
    return fig        
    
# OUTPUT FIGURE OBJECT
def saveFig(fig, filePath, filename, figure_parameter):
    """画像を保存する
    入力:figureオブジェクト, ファイル名, データセット名"""
    os.makedirs(filePath, exist_ok=True)
    fig.savefig(filePath + "/" + filename, transparent=figure_parameter.IS_TRANSPARENT) 
    
def plotRuleSetting(ax, figure_parameter):
    ax.set_xlim([-0.05, 1.05])
    ax.set_ylim([-0.05, 1.05])
    if figure_parameter.PLOT_TICK_FLAG:
        ax.tick_params(axis="x", labelsize=figure_parameter.TICK_FONTSIZE)
        ax.tick_params(axis="y", labelsize=figure_parameter.TICK_FONTSIZE)
    else:
        ax.tick_params(labelbottom=False, labelleft=False)
        
    if figure_parameter.PLOT_LABEL_FLAG:
        ax.set_xlabel("属性値", fontsize=figure_parameter.LABEL_FONTSIZE,  fontname="MS Gothic")
        ax.set_ylabel("メンバシップ値", fontsize=figure_parameter.LABEL_FONTSIZE,  fontname="MS Gothic")
        
    if figure_parameter.PLOT_GRID_FLAG:
        ax.grid(True)
        
    if figure_parameter.PLOT_LEGEND_FLAG:
        ax.legend().get_frame().set_alpha(1.0)
        
def plotResultSetting(ax, figure_parameter):
    if figure_parameter.PLOT_TICK_FLAG:
        ax.tick_params(axis="x", labelsize=figure_parameter.TICK_FONTSIZE)
        ax.tick_params(axis="y", labelsize=figure_parameter.TICK_FONTSIZE)
    else:
        ax.tick_params(labelbottom=False, labelleft=False)
        
    if figure_parameter.PLOT_LABEL_FLAG:
        ax.set_xlabel("ルール数", fontsize=figure_parameter.LABEL_FONTSIZE,  fontname="MS Gothic")
        ax.set_ylabel("誤識別率[%]", fontsize=figure_parameter.LABEL_FONTSIZE,  fontname="MS Gothic")
        
    if figure_parameter.PLOT_LEGEND_FLAG:
        ax.legend().get_frame().set_alpha(1.0)
        
    if figure_parameter.PLOT_GRID_FLAG:
        ax.grid(True)
##############################################################################
    
class Pattern:
    def __init__(self, attributeList, classLabel):
        self.attributeList = attributeList
        self.classLabel = classLabel

class Dat:
    def __init__(self, dataName, i, j, ROOTFOLDER=ROOTFOLDER):
        train_data = open(ROOTFOLDER + "/dataset/" + dataName + "/a{}_{}_{}-10tra.dat".format(i,j, dataName), 'r')
        test_data = open(ROOTFOLDER + "/dataset/" + dataName + "/a{}_{}_{}-10tst.dat".format(i,j, dataName), 'r')
        
        self.dtra = []
        for i, rows in enumerate(train_data):
            row = rows.rstrip('\n').split(',')[:-1]
            attributeList = row[:-1]
            classLabel = int(row[-1])
            self.dtra.append(Pattern(attributeList, classLabel))
        self.dtst = []
        for i, rows in enumerate(test_data):
            row = rows.rstrip('\n').split(',')[:-1]
            attributeList = row[:-1]
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
        self.parameters = {int(parameters_i.get('id')) : float(parameters_i.text) for parameters_i in fuzzyTerm.find('parameterSet')}
    
    def setAx(self, ax, alpha = 1.0, alpha_between = 0.3, color = "black", color_between = "blue", input_x=None):
        """Ax にメンバシップ関数をプロットする"""
        color_buf = "C{}".format(color) if type(color) is int else color
        
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
        return self.michiganSolutions[michiganSolutionID].getFuzzyTermID[dimID]
    
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
    
    def plotRule(self, knowledge, figure_parameter, ruleIDList=None, coloring='class', inputPattern = None):
        if ruleIDList is not None: ruleIDList = range(len(self.michiganSolutions))
        if type(ruleIDList) is int: ruleIDList = [ruleIDList]
        
        """１つの画像に全てのif-thenルールをプロット"""
        fig = multiFig_set(y_FigNum = len(self.ruleIDList), x_FigNum=self.consts.getParameter('ATTRIBUTE_NUMBER'), title='PittsburghSolution')
        for rule_i in ruleIDList:
            for dim_i, ax in enumerate(fig.axes):
                
                plotRuleSetting(ax, figure_parameter)
                
                if coloring == 'each rules': color_between = cmap(rule_i)
                elif coloring == 'class': color_between = cmap(self.getMichiganSolution(rule_i).consequentClass)
                elif coloring == 'single': color_between = cmap(0)
                
                FuzzyTermID = self.getFuzzyTermID(rule_i, dim_i)
                
                if inputPattern is not None:
                    knowledge.getFuzzyTerm(dim_i, FuzzyTermID).setAx(ax, color_between=color_between, input_x=inputPattern[dim_i])
                else:
                    knowledge.getFuzzyTerm(dim_i, FuzzyTermID).setAx(ax, color_between=color_between)
        saveFig(fig, figure_parameter.savePath, 'PittsburghSolution', figure_parameter)
        plt.close()
        
    def clacFitness(self, knowledge, data):
        """誤識別率を出力"""
        buf = [0]*len(self.michiganSolutions)
        for michiganSolutionID, michiganSolution in self.michiganSolutions.items():
            tmp = 1
            for dimID, FuzzyTermID in enumerate(michiganSolution.getFuzzyTermIDList()):
                tmp2 = knowledge.membershipValue(dimID, FuzzyTermID, data[dimID])
                tmp *= tmp2
                print( 'x_{} is {:3f}'.format(dimID, tmp2), end=' ')
            tmp *= michiganSolution.ruleWeight
            buf[michiganSolutionID] = (tmp, michiganSolution.consequentClass)
            print('then (Class{}, {:5f}) with {:5f}'.format(michiganSolution.consequentClass, tmp, michiganSolution.ruleWeight))
        return buf    
    
    def clacConclusionClass(self, data):
        fitness = self.clacFitness(data)
        buf_fitness = -1
        buf_class = -1
        for tmp in fitness:
            if buf_fitness < tmp[0]:
                buf_fitness = tmp[0]
                buf_class = tmp[1]
        return buf_class
    
    def calcConclusionClassJudge(self, pattern):
        classBuf = self.clacConclusionClass(pattern.attributeList)
        return classBuf == pattern.classLabel
    
    def calcErrorRate(self, Dat, isDtra):
        cnt = 0
        if isDtra:
            for pattern_i in Dat.dtra:
                if self.calcConclusionClassJudge(pattern_i): cnt += 1
            return float(cnt)/len(Dat.dtra)
        else:
            for pattern_i in Dat.dtst:
                if self.calcConclusionClassJudge(pattern_i): cnt += 1
            return float(cnt)/len(Dat.dtst)
    
        
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
            fig.subplots_adjust(bottom=0.12)
            ax = fig.gca()
            for experimentLabel in experimentLabelList:
                experimentManager = self.master[experimentLabel]
                data = experimentManager.getPittsburghObjectives_Allsolutions(evaluation=evaluation, RULE_BOOK=RULE_BOOK)
                print(data)
                ax.scatter([data_i[0] for data_i in data], [data_i[1] for data_i in data], s=FIGURE_PARAMETER.MARKER_SIZE, alpha=FIGURE_PARAMETER.MARKER_ALPHA, label=experimentLabel)

        elif mode=='Alltrials':
            fig = singleFig_set(FIGURE_PARAMETER, 'Alltrials' + str(evaluation))
            fig.subplots_adjust(bottom=0.12)
            ax = fig.gca()        
            for experimentLabel in experimentLabelList:
                experimentManager = self.master[experimentLabel]
                data = experimentManager.getPittsburghObjectives_Alltrials(evaluation=evaluation, RULE_BOOK=RULE_BOOK)
                ax.scatter([data_i[0] for data_i in data], [data_i[1] for data_i in data], s=FIGURE_PARAMETER.MARKER_SIZE, alpha=FIGURE_PARAMETER.MARKER_ALPHA, label=experimentLabel)                
        
        elif mode=='Average':
            fig = singleFig_set(FIGURE_PARAMETER, 'Average' + str(evaluation))
            fig.subplots_adjust(bottom=0.12)
            ax = fig.gca()        
            for experimentLabel in experimentLabelList:
                experimentManager = self.master[experimentLabel]
                data = experimentManager.getPittsburghObjectives_Average(evaluation=evaluation, RULE_BOOK=RULE_BOOK)
                print(data)
                ax.scatter([data_i[0] for data_i in data], [data_i[1] for data_i in data], s=FIGURE_PARAMETER.MARKER_SIZE, alpha=FIGURE_PARAMETER.MARKER_ALPHA, label=experimentLabel)
                
        plotResultSetting(ax, FIGURE_PARAMETER)
        
        if saveFigFlag:
            dataType = "Dtra" if RULE_BOOK.IS_DTRA else "Dtst"
            fileName = "result_{:s}_{:s}_{:08}".format(self.dataName, dataType, evaluation)
            saveFig(fig, self.ROOTFOLDER + "/results/graph/" + self.dataName + "/", fileName, FIGURE_PARAMETER)
        plt.show()
        
###############################################################################

FIGURE_PARAMETER = FIGURE_PARAMETER_CLASS()
RULE_BOOK = RULE_BOOK_CLASS()